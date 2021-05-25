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
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;

import java.math.BigDecimal;
import java.util.List;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.getTypeFromId;

@Service
@RequiredArgsConstructor
public class ModifyItemService implements ModifyItemUseCase {

    private final ItemJpaRepository itemRepository;
    private final IngredientJpaRepository ingredientRepository;
    private final UserRepository userRepository;
    private final UserEntity masterUser;

    @Override
    @Transactional
    public Response<ItemDto> createItem(CreateNewItemCommand command)  {

        if (nameAlreadyTaken(command)) return Response.failure("You already have an item called " + command.getItemName());
        Item item = newItemCommandToItem(command);
        generateRecipeIfApplicable(item);
        return  Response.success(convertToDto(itemRepository.save(item)));
    }

    private boolean nameAlreadyTaken(CreateNewItemCommand command)  {
        return itemRepository.findFirstByNameAndUserEntityId(command.getItemName(), command.getUserId()).isPresent();

    }

    private Item newItemCommandToItem(CreateNewItemCommand command) {
        return new Item(command.getItemName(), Unit.getUnitFromId(command.getItemUnitId()), getTypeFromId(command.getItemTypeId()), userRepository.getOne(command.getUserId()));
    }


    @Transactional
    @Override
    public Response<RichItem> addIngredientToRecipe(AddIngredientCommand command, Long userId)  {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Item childItem = itemRepository.getOne(command.getChildItemId());

        Response verifyLoops = checkForLoops(parentItem, childItem);
        if (!verifyLoops.isSuccess()) return Response.failure(verifyLoops.getError());

        Response<Null> verifyOwnership = confirmOwnership(parentItem, childItem, userId);
        if (!verifyOwnership.isSuccess()) return Response.failure(verifyOwnership.getError());

        addIngredient(parentItem, childItem, command.getAmount());

        return  Response.success(new RichItem(itemRepository.save(parentItem)));
    }
    private Response<Null> checkForLoops(Item parentItem, Item childItem)  {
        if (childItem.getDependencies().contains(parentItem))
            return Response.failure(parentItem.getName() + " is already a dependency of " + childItem.getName());
        if (childItem.equals(parentItem)) {
            return Response.failure("An item cannot depend on itself");
        }
        return Response.success(null);
    }

    private Response<Null> confirmOwnership(Item parentItem, Item childItem, Long userId) {
        if (!parentItem.getUserEntity().getId().equals(userId))
            return Response.failure("You're not authorized to modify this item");
        if ((!childItem.getUserEntity().getId().equals(userId)) &&
             !childItem.getUserEntity().equals(masterUser))
            return Response.failure("You do not own this ingredient");

        return Response.success(null);
    }


    @Override
    @Transactional
    public RichItem setYield(SetYieldCommand command) {
        Item item = itemRepository.findById(command.getParentItemId()).orElseThrow();
        item.getRecipe().setRecipeYield(command.getItemYield());
        return new RichItem(itemRepository.save(item));
    }

    @Override
    @Transactional
    public RichItem updateDescription(UpdateDescriptionCommand command) {
        Item item = itemRepository.getOne(command.getParentItemId());
        item.getRecipe().setDescription(command.getDescription());
        return new RichItem(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(DeleteItemCommand command) {

        removeThisItemFromAllDependencies(command.getItemId());

        itemRepository.deleteById(command.getItemId());

    }

    private void removeThisItemFromAllDependencies(Long itemId) {

        List<Ingredient> ingredients = ingredientRepository.findAllByChildItemId(itemId);

        for (Ingredient ingredient : ingredients) {
            ingredient.removeSelf();
        }

        ingredientRepository.findAllByChildItemId(itemId)
                .forEach(Ingredient::removeSelf);
    }

    @Override
    @Transactional
    public RichItem removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Ingredient ingredientToRemove = ingredientRepository.getOne(command.getIngredientId());

        ingredientToRemove.removeSelf();

        return new RichItem(itemRepository.save(parentItem));
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
