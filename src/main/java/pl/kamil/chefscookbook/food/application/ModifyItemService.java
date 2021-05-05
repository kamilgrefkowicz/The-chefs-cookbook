package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.entity.Recipe;

import javax.transaction.Transactional;
import javax.validation.Valid;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.*;
import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem.toRichItem;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Service
@RequiredArgsConstructor
public class ModifyItemService implements ModifyItemUseCase {

    private final ItemJpaRepository itemRepository;

    @Override
    @Transactional
    public RichItem createItem(CreateNewItemCommand command) {
        Item item = command.toItem();
        generateRecipeIfApplicable(item);
        return toRichItem(itemRepository.save(item));
    }

    @Transactional
    @Override
    public RichItem addIngredientToRecipe(AddIngredientCommand command) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Item childItem = itemRepository.getOne(command.getChildItemId());
        //todo implement exceptions
        checkForLoops(parentItem, childItem);
        addIngredient(parentItem, childItem, command.getAmount());
        activateIfValid(parentItem);



        return toRichItem(itemRepository.save(parentItem));
    }

    @Override
    @Transactional
    public RichItem setYield(SetYieldCommand command) {
        Item item = itemRepository.getOne(command.getParentItemId());
        item.getRecipe().setDefaultYield(command.getYield());
        activateIfValid(item);
        return toRichItem(itemRepository.save(item));
    }

    private void activateIfValid(Item parentItem) {
        if (parentItem.isActive()) return;
        if (!parentItem.getIngredients().isEmpty() && parentItem.getRecipe().getDefaultYield() != null)
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

    private void activateItemIfRecipeIsComplete(Recipe recipe) {

    }


    private void generateRecipeIfApplicable(Item item) {
        if (!item.getType().equals(BASIC())) item.setRecipe(new Recipe());
    }
}
