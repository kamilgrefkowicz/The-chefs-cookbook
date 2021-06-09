package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.database.IngredientJpaRepository;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;

import java.security.Principal;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.*;

@Service
@RequiredArgsConstructor
public class ModifyItemService implements ModifyItemUseCase {

    private final ItemJpaRepository itemRepository;
    private final IngredientJpaRepository ingredientRepository;
    private final UserRepository userRepository;
    private final UserSecurityUseCase userSecurity;

    @Override
    @Transactional
    public Response<ItemDto> createItem(CreateNewItemCommand command, Principal user) {

        if (nameAlreadyTaken(command, user))
            return Response.failure(ITEM_NAME_TAKEN);

        Item item = newItemCommandToItem(command, user);

        return Response.success(convertToDto(itemRepository.save(item)));
    }

    private boolean nameAlreadyTaken(CreateNewItemCommand command, Principal user) {
        return itemRepository.findFirstByNameAndUserEntityId(command.getItemName(), Long.valueOf(user.getName())).isPresent();

    }

    private Item newItemCommandToItem(CreateNewItemCommand command, Principal user) {

        return  new Item(command.getItemName(),
                command.getUnit(),
                command.getType(),
                userRepository.getOne(Long.valueOf(user.getName())));

    }


    @Transactional
    @Override
    public Response<RichItem> addIngredientToRecipe(AddIngredientCommand command, Principal user) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Item childItem = itemRepository.getOne(command.getChildItemId());

        boolean eligibilityValidation = userSecurity.validateEligibilityForAddIngredient(parentItem, childItem, user);
        if (!eligibilityValidation) return Response.failure(NOT_AUTHORIZED);

        Response<Void> verifyLoops = checkForLoops(parentItem, childItem);
        if (!verifyLoops.isSuccess()) return Response.failure(verifyLoops.getError());

        parentItem.addIngredient(childItem, command.getAmount());

        return Response.success(new RichItem(itemRepository.save(parentItem)));
    }

    private Response<Void> checkForLoops(Item parentItem, Item childItem) {
        if (childItem.getDependencies().contains(parentItem))
            return Response.failure(parentItem.getName() + LOOP_LONG + childItem.getName());
        if (childItem.equals(parentItem)) {
            return Response.failure(LOOP_SHORT);
        }
        return Response.success(null);
    }


    @Override
    @Transactional
    public Response<RichItem> setYield(SetYieldCommand command, Principal user) {
        Item item = itemRepository.findById(command.getParentItemId()).orElseThrow();
        if (!userSecurity.isOwner(item.getUserEntity().getId(), user))
            return Response.failure(NOT_AUTHORIZED);

        item.getRecipe().setRecipeYield(command.getItemYield());
        return Response.success(new RichItem(itemRepository.save(item)));
    }

    @Override
    @Transactional
    public Response<RichItem> updateDescription(UpdateDescriptionCommand command, Principal user) {
        Item item = itemRepository.getOne(command.getParentItemId());
        if (!userSecurity.isOwner(item.getUserEntity().getId(), user))
            return Response.failure(NOT_AUTHORIZED);
        item.getRecipe().setDescription(command.getDescription());
        return Response.success(new RichItem(itemRepository.save(item)));

    }

    @Override
    @Transactional
    public Response<Void> deleteItem(DeleteItemCommand command, Principal user) {

        Item item = itemRepository.getOne(command.getItemId());

        if (!userSecurity.isOwner(item.getUserEntity().getId(), user))
            return Response.failure(NOT_AUTHORIZED);

        removeThisItemFromAllDependencies(command.getItemId());

        itemRepository.deleteById(command.getItemId());
        return Response.success(null);

    }

    private void removeThisItemFromAllDependencies(Long itemId) {
        ingredientRepository.findAllByChildItemId(itemId)
                .forEach(Ingredient::removeSelf);
    }

    @Override
    @Transactional
    public Response<RichItem> removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command, Principal user) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Ingredient ingredientToRemove = ingredientRepository.getOne(command.getIngredientId());

        if (!userSecurity.isOwner(parentItem.getUserEntity().getId(), user))
            return Response.failure(NOT_AUTHORIZED);

        ingredientToRemove.removeSelf();

        return Response.success(new RichItem(itemRepository.save(parentItem)));
    }



}
