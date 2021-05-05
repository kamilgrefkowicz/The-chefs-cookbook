package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ModifyItemServiceTest {

    @Autowired
    private ModifyItemService modifyItem;

    @Autowired
    private QueryItemService queryItem;


    @Test
    void canCreateItem() {
        givenItemCreated("Butter", BASIC());

        RichItem item = queryItem.findById(1L);

        assertEquals("Butter", item.getName());
        assertEquals(BASIC(), item.getType());
        assertFalse(item.isActive());
        assertNull(item.getRecipe());
    }

    @Test
    void creatingIntermediateOrDishItemShouldGenerateEmptyRecipe() {
        givenItemCreated("Dish", DISH());
        givenItemCreated("Intermediate", INTERMEDIATE());

        RichItem intermediate = queryItem.findById(1L);
        RichItem dish = queryItem.findById(2L);

        assertNotNull(intermediate.getRecipe());
        assertNotNull(dish.getRecipe());
    }

    @Test
    void canAddIngredientToRecipe() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());
        var butter = givenItemCreated("Butter", BASIC());

        modifyItem.addIngredientToRecipe(new ModifyItemUseCase.AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        var queried  = queryItem.findById(puree.getId());

        assertFalse(queried.getRecipe().getIngredients().isEmpty());
    }

    private RichItem givenItemCreated(String itemName, Type itemType) {
        return modifyItem.createItem(new CreateNewItemCommand(itemName, itemType));
    }

    @Test
    void addingSameIngredientToRecipeShouldAggregateAmounts() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());
        var butter = givenItemCreated("Butter", BASIC());
        modifyItem.addIngredientToRecipe(new ModifyItemUseCase.AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));

        modifyItem.addIngredientToRecipe(new ModifyItemUseCase.AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        var queried = queryItem.findById(puree.getId());

        assertEquals(1, queried.getRecipe().getIngredients().size());
        assertEquals("2.00", queried.getRecipe().getIngredients().stream().findFirst().get().getAmount().toPlainString());
    }

    @Test
    void attemptingToFormALoopShouldThrowAnException() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());

        assertThrows(IllegalArgumentException.class, () -> modifyItem.addIngredientToRecipe(new ModifyItemUseCase.AddIngredientCommand(puree.getId(), puree.getId(), BigDecimal.ONE)));

    }
    @Test
    void dishOrIntermediateItemShouldBecomeActiveWhenBothYieldSetAndAtLeastOneIngredient() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());
        var butter = givenItemCreated("Butter", BASIC());

        modifyItem.addIngredientToRecipe(new ModifyItemUseCase.AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        modifyItem.setYield(new ModifyItemUseCase.SetYieldCommand(puree.getId(), BigDecimal.valueOf(1)));

        var queried = queryItem.findById(puree.getId());

        assertTrue(queried.isActive());

    }


}