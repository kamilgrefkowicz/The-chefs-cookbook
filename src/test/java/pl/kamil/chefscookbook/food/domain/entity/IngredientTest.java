package pl.kamil.chefscookbook.food.domain.entity;

import org.junit.jupiter.api.Test;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;

class IngredientTest {

    @Test
    void getRatioShouldReturnCorrectValue() {
        Item parentItem = new Item("testItem", PIECE, DISH, new UserEntity());
        parentItem.getRecipe().setRecipeYield(BigDecimal.ONE);
        Item childItem = new Item("testChild", KILOGRAM, INTERMEDIATE, new UserEntity());
        parentItem.addIngredient(childItem, new BigDecimal(2));

        BigDecimal ratio = parentItem.getIngredients().stream().findFirst().get().getRatio();

        assertThat(ratio, comparesEqualTo(new BigDecimal(2)));
    }

    @Test
    void removeSelfShouldRemoveIngredientFromRecipe() {
        Item parentItem = new Item("testItem", PIECE, DISH, new UserEntity());
        Item childItem = new Item("testChild", KILOGRAM, INTERMEDIATE, new UserEntity());
        parentItem.addIngredient(childItem, new BigDecimal(2));

        parentItem.getIngredients().stream().findFirst().get().removeSelf();

        assertThat(parentItem.getIngredients(), hasSize(0));
    }
}