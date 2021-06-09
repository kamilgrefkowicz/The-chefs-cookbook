package pl.kamil.chefscookbook.food.application.dto.item;

import org.junit.jupiter.api.Test;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.entity.Recipe;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;

class RichItemTest {

    @Test
    void canMapToRichItem() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        Item parentItem = new Item("testItem", PIECE, DISH, user);
        parentItem.setId(2L);
        parentItem.getRecipe().setRecipeYield(BigDecimal.ONE);
        parentItem.getRecipe().setDescription("test description");

        Item childItem = new Item("testChild", KILOGRAM, INTERMEDIATE, user);
        parentItem.addIngredient(childItem, new BigDecimal(3));

        RichItem toTest = new RichItem(parentItem);

        assertThat(toTest.getId(), equalTo(2L));
        assertThat(toTest.getName(), equalTo("testItem"));
        assertThat(toTest.getUnit(), equalTo(PIECE));
        assertThat(toTest.getType(), equalTo(DISH));
        assertThat(toTest.getUserEntityId(), equalTo(1L));
        assertThat(toTest.getIngredients(), hasSize(1));
        assertThat(toTest.getDescription(), equalTo("test description"));
        assertThat(toTest.getRecipeYield(), equalTo(BigDecimal.ONE));
    }
}