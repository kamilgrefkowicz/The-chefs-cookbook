package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
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
        if (loopsFound(parentItem, childItem)) throw new IllegalArgumentException();

        parentItem.getRecipe().getIngredients().add(new Ingredient(parentItem.getRecipe(), childItem, command.getAmount()));

        if (!parentItem.isActive()) activateItemIfRecipeIsComplete(parentItem.getRecipe());

        return toRichItem(itemRepository.save(parentItem));
    }

    private void activateItemIfRecipeIsComplete(Recipe recipe) {
        if (!recipe.getIngredients().isEmpty() && recipe.getDefaultYield() != null) recipe.getParentItem().setActive(true);
    }

    private boolean loopsFound(Item parentItem, Item childItem) {
        return childItem.getDependencies().contains(parentItem) || childItem.equals(parentItem);
    }


    private void generateRecipeIfApplicable(Item item) {
        if (!item.getType().equals(BASIC())) item.setRecipe(new Recipe());
    }
}
