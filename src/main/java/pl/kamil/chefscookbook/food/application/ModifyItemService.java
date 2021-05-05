package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.database.IngredientJpaRepository;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.entity.Recipe;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.*;
import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem.toRichItem;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Service
@RequiredArgsConstructor
public class ModifyItemService implements ModifyItemUseCase {

    private final ItemJpaRepository itemRepository;
    private final IngredientJpaRepository ingredientRepository;

    @Override
    @Transactional
    public RichItem createItem(CreateNewItemCommand command) {
        Item item = command.toItem();
        generateRecipeIfApplicable(item);
        setToActiveIsBasic(item);
        return toRichItem(itemRepository.save(item));
    }


    @Transactional
    @Override
    public RichItem addIngredientToRecipe(AddIngredientCommand command) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Item childItem = itemRepository.getOne(command.getChildItemId());
        //todo implement exceptions
        ensureChildItemActive(childItem);
        checkForLoops(parentItem, childItem);
        addIngredient(parentItem, childItem, command.getAmount());
        activateIfValid(parentItem);

        return toRichItem(itemRepository.save(parentItem));
    }
    private void setToActiveIsBasic(Item item) {
        if (item.getType().equals(BASIC())) item.setActive(true);
    }

    private void ensureChildItemActive(Item childItem) {
        if (!childItem.isActive()) throw new IllegalArgumentException();
    }

    @Override
    @Transactional
    public RichItem setYield(SetYieldCommand command) {
        Item item = itemRepository.getOne(command.getParentItemId());
        item.getRecipe().setRecipeYield(command.getItemYield());
        activateIfValid(item);
        return toRichItem(itemRepository.save(item));
    }

    @Override
    @Transactional
    public RichItem updateDescription(UpdateDescriptionCommand command) {
        Item item = itemRepository.getOne(command.getParentItemId());
        item.getRecipe().setDescription(command.getDescription());
        return toRichItem(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(DeleteItemCommand command) {
        itemRepository.deleteById(command.getItemId());
    }

    @Override
    @Transactional
    public RichItem removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Ingredient ingredientToRemove = ingredientRepository.getOne(command.getIngredientId());

        parentItem.getRecipe().getIngredients().remove(ingredientToRemove);
        inactivateItemIfBecameInvalid(parentItem);

        return toRichItem(itemRepository.save(parentItem));
    }

    private void inactivateItemIfBecameInvalid(Item parentItem) {
        if (parentItem.getIngredients().isEmpty()) parentItem.setActive(false);
    }

    private void activateIfValid(Item parentItem) {
        if (parentItem.isActive()) return;
        if (!parentItem.getIngredients().isEmpty() && parentItem.getRecipe().getRecipeYield() != null)
            parentItem.setActive(true);
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

    private void checkForLoops(Item parentItem, Item childItem) {
        if ((childItem.getDependencies().contains(parentItem) || childItem.equals(parentItem)))
            throw new IllegalArgumentException();
    }



    private void generateRecipeIfApplicable(Item item) {
        if (!item.getType().equals(BASIC())) item.setRecipe(new Recipe());
    }
}
