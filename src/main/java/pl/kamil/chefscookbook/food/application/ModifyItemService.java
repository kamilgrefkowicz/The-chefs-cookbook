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
import pl.kamil.chefscookbook.food.domain.entity.Recipe;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.security.Principal;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.getTypeFromId;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.getUnitFromId;

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
            return Response.failure("You already have an item called " + command.getItemName());

        Item item = newItemCommandToItem(command, user);
        generateRecipeIfApplicable(item);
        return Response.success(convertToDto(itemRepository.save(item)));
    }

    private boolean nameAlreadyTaken(CreateNewItemCommand command, Principal user) {
        return itemRepository.findFirstByNameAndUserEntityId(command.getItemName(), Long.valueOf(user.getName())).isPresent();

    }

    private Item newItemCommandToItem(CreateNewItemCommand command, Principal user) {
        return Item.builder()
                .name(command.getItemName())
                .unit(getUnitFromId(command.getItemUnitId()))
                .type(getTypeFromId(command.getItemTypeId()))
                .userEntity(userRepository.getOne(Long.valueOf(user.getName())))
                .build();
    }


    @Transactional
    @Override
    public Response<RichItem> addIngredientToRecipe(AddIngredientCommand command, Principal user) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Item childItem = itemRepository.getOne(command.getChildItemId());

        Response<Void> eligibilityValidation = userSecurity.validateEligibilityForAddIngredient(parentItem, childItem, user);
        if (!eligibilityValidation.isSuccess()) return Response.failure(eligibilityValidation.getError());

        Response<Void> verifyLoops = checkForLoops(parentItem, childItem);
        if (!verifyLoops.isSuccess()) return Response.failure(verifyLoops.getError());

        addIngredient(parentItem, childItem, command.getAmount());

        return Response.success(new RichItem(itemRepository.save(parentItem)));
    }

    private Response<Void> checkForLoops(Item parentItem, Item childItem) {
        if (childItem.getDependencies().contains(parentItem))
            return Response.failure(parentItem.getName() + " is already a dependency of " + childItem.getName());
        if (childItem.equals(parentItem)) {
            return Response.failure("An item cannot depend on itself");
        }
        return Response.success(null);
    }


    @Override
    @Transactional
    public Response<RichItem> setYield(SetYieldCommand command, Principal user) {
        Item item = itemRepository.findById(command.getParentItemId()).orElseThrow();
        if (!userSecurity.isOwner(item.getUserEntity().getId(), user))
            return Response.failure("You do not own this item");

        item.getRecipe().setRecipeYield(command.getItemYield());
        return Response.success(new RichItem(itemRepository.save(item)));
    }

    @Override
    @Transactional
    public Response<RichItem> updateDescription(UpdateDescriptionCommand command, Principal user) {
        Item item = itemRepository.getOne(command.getParentItemId());
        if (!userSecurity.isOwner(item.getUserEntity().getId(), user))
            return Response.failure("You're not authorized to modify this item");
        item.getRecipe().setDescription(command.getDescription());
        return Response.success(new RichItem(itemRepository.save(item)));

    }

    @Override
    @Transactional
    public void deleteItem(DeleteItemCommand command) {

        removeThisItemFromAllDependencies(command.getItemId());

        itemRepository.deleteById(command.getItemId());

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
            return Response.failure("You do not own this item");

        ingredientToRemove.removeSelf();

        return Response.success(new RichItem(itemRepository.save(parentItem)));
    }


    private void addIngredient(Item parentItem, Item childItem, BigDecimal amount) {
        for (Ingredient ingredient : parentItem.getIngredients()) {
            if (ingredient.getChildItem().equals(childItem)) {
                ingredient.setAmount(ingredient.getAmount().add(amount));
                return;
            }
        }
        parentItem.getIngredients().add(new Ingredient(parentItem.getRecipe(), childItem, amount));
    }


    private void generateRecipeIfApplicable(Item item) {
        if (!item.getType().equals(BASIC())) item.setRecipe(new Recipe(BigDecimal.ONE));
    }
}
