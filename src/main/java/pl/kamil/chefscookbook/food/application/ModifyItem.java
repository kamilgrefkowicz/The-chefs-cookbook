package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService;
import pl.kamil.chefscookbook.food.database.IngredientRepository;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.*;

@Service
@RequiredArgsConstructor
public class ModifyItem implements ModifyItemService {

    private final ItemRepository itemRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final UserSecurityService userSecurity;

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

        return new Item(
                command.getItemName(),
                getUnitOrInferPiece(command),
                command.getType(),
                userRepository.getOne(Long.valueOf(user.getName())));

    }

    private Unit getUnitOrInferPiece(CreateNewItemCommand command) {
        if (command.getType().equals(DISH)) return PIECE;
        return command.getUnit();
    }


    @Transactional
    @Override
    public Response<ItemDto> addIngredientToRecipe(AddIngredientCommand command, Principal user) {

        if (command.getChildItemId() == null) return Response.failure(CHOOSE_FROM_LIST);

        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Optional<Item> optionalChild = itemRepository.findById(command.getChildItemId());

        if (optionalChild.isEmpty()) return Response.failure(NOT_FOUND);
        Item childItem = optionalChild.get();

        if (!isEligible(user, parentItem, childItem)) return Response.failure(NOT_AUTHORIZED);

        Response<Void> verifyLoops = checkForLoops(parentItem, childItem);
        if (!verifyLoops.isSuccess()) return Response.failure(verifyLoops.getError());

        parentItem.addIngredient(childItem, command.getAmount());
        itemRepository.save(parentItem);

        return successfulResponse(parentItem, INGREDIENT_ADDED);
    }

    private boolean isEligible(Principal user, Item parentItem, Item childItem) {
        return (userSecurity.belongsTo(parentItem, user) && userSecurity.belongsToOrIsPublic(childItem, user));
    }

    private Response<Void> checkForLoops(Item parentItem, Item childItem) {
        if (getItemDependencies(childItem).contains(parentItem))
            return Response.failure(parentItem.getName() + LOOP_LONG + childItem.getName());
        if (childItem.equals(parentItem)) {
            return Response.failure(LOOP_SHORT);
        }
        return Response.success(null);
    }

    private Collection<Item> getItemDependencies(Item childItem) {
        if (childItem.getType().equals(BASIC)) return Collections.emptySet();
        Set<Item> dependencies = childItem.getIngredients()
                .stream()
                .map(Ingredient::getChildItem)
                .collect(Collectors.toSet());

        dependencies
                .forEach(item -> dependencies.addAll(getItemDependencies(item)));

        return dependencies;
    }

    @Override
    @Transactional
    public Response<ItemDto> setYield(SetYieldCommand command, Principal user) {
        Optional<Item> optional = itemRepository.findById(command.getParentItemId());
        if (optional.isEmpty()) return Response.failure(NOT_FOUND);
        Item item = optional.get();
        if (!userSecurity.belongsTo(item, user)) return Response.failure(NOT_AUTHORIZED);

        item.getRecipe().setRecipeYield(command.getItemYield());
        return successfulResponse(item, YIELD_SET);
    }

    @Override
    @Transactional
    public Response<ItemDto> updateDescription(UpdateDescriptionCommand command, Principal user) {
        Optional<Item> optional = itemRepository.findById(command.getParentItemId());
        if (optional.isEmpty()) return Response.failure(NOT_FOUND);
        Item item = optional.get();
        if (!userSecurity.belongsTo(item, user)) return Response.failure(NOT_AUTHORIZED);

        item.getRecipe().setDescription(command.getDescription());
        return successfulResponse(item, DESCRIPTION_MODIFIED);

    }

    private Response<ItemDto> successfulResponse(Item item) {
        return Response.success(new RichItem(itemRepository.save(item)));
    }
    private Response<ItemDto> successfulResponse(Item item, String message) {
        return Response.success(new RichItem(item), message);
    }

    @Override
    @Transactional
    public Response<Void> deleteItem(DeleteItemCommand command, Principal user) {

        Optional<Item> optional = itemRepository.findById(command.getItemId());
        if (optional.isEmpty()) return Response.failure(NOT_FOUND);
        Item item = optional.get();
        if (!userSecurity.belongsTo(item, user)) return Response.failure(NOT_AUTHORIZED);

        removeThisItemFromAllDependencies(command.getItemId());

        itemRepository.deleteById(command.getItemId());
        return Response.success(null, ITEM_DELETED);

    }

    private void removeThisItemFromAllDependencies(Long itemId) {
        ingredientRepository.findAllByChildItemId(itemId)
                .forEach(Ingredient::removeSelf);
    }

    @Override
    @Transactional
    public Response<ItemDto> removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command, Principal user) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Ingredient ingredientToRemove = ingredientRepository.getOne(command.getIngredientId());

        if (!isEligible(user, parentItem, ingredientToRemove.getChildItem()))
            return Response.failure(NOT_AUTHORIZED);

        ingredientToRemove.removeSelf();

        return successfulResponse(parentItem, INGREDIENT_REMOVED);
    }


}
