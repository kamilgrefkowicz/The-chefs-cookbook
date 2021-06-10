package pl.kamil.chefscookbook.food.domain.entity;

import org.junit.jupiter.api.Test;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;

class ItemTest {

    @Test
    void creatingABasicItemDoesNotGenerateRecipe() {
        Item item = new Item("testItem", KILOGRAM, BASIC, new UserEntity());

        assertNull(item.getRecipe());
    }

    @Test
    void creatingAnIntermediateOrDishItemGeneratesRecipe() {
        Item item = new Item("testItem", KILOGRAM, DISH, new UserEntity());

        assertNotNull(item.getRecipe());
        assertThat(item.getRecipe().getParentItem(), equalTo(item));
    }

    @Test
    void addingNewIngredientShouldIncreaseSetSize() {
        Item item = new Item("test item", KILOGRAM, DISH, new UserEntity());
        Item childItem = new Item("test child item", KILOGRAM, INTERMEDIATE, new UserEntity());

        item.addIngredient(childItem, BigDecimal.ONE);

        assertThat(item.getIngredients(), hasSize(1));
    }
    @Test
    void addingAlreadyPresentIngredientAggregatesAmount() {
        Item item = new Item("test item", KILOGRAM, DISH, new UserEntity());
        Item childItem = new Item("test child item", KILOGRAM, INTERMEDIATE, new UserEntity());

        item.addIngredient(childItem, BigDecimal.ONE);
        item.addIngredient(childItem, BigDecimal.ONE);

        assertThat(item.getIngredients(), hasSize(1));
        assertThat(item.getIngredients().stream().findFirst().get().getAmount(), equalTo(new BigDecimal(2)));
    }

}