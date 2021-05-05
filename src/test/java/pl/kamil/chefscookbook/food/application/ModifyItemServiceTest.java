package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.RemoveIngredientFromRecipeCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.UpdateDescriptionCommand;
import pl.kamil.chefscookbook.food.database.IngredientJpaRepository;
import pl.kamil.chefscookbook.food.domain.staticData.Type;

import java.math.BigDecimal;

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

    @Autowired
    IngredientJpaRepository ingredientJpaRepository;


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

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
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
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        var queried = queryItem.findById(puree.getId());

        assertEquals(1, queried.getRecipe().getIngredients().size());
        assertEquals("2.00", queried.getRecipe().getIngredients().stream().findFirst().get().getAmount().toPlainString());
    }

    @Test
    void attemptingToFormALoopShouldThrowAnException() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());
        AddIngredientCommand command = new AddIngredientCommand(puree.getId(), puree.getId(), BigDecimal.ONE);

        assertThrows(IllegalArgumentException.class, () ->  modifyItem.addIngredientToRecipe(command));

    }
    @Test
    void dishOrIntermediateItemShouldBecomeActiveWhenBothYieldSetAndAtLeastOneIngredient() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());
        var butter = givenItemCreated("Butter", BASIC());

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        modifyItem.setYield(new ModifyItemUseCase.SetYieldCommand(puree.getId(), BigDecimal.valueOf(1)));

        var queried = queryItem.findById(puree.getId());

        assertTrue(queried.isActive());
    }

    @Test
    void canUpdateDescription() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());

        modifyItem.updateDescription(new UpdateDescriptionCommand(puree.getId(), "description"));
        var queried = queryItem.findById(puree.getId());

        assertEquals("description", queried.getRecipe().getDescription());
    }
    @Test
    void canDeleteItem() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());

        modifyItem.deleteItem(new ModifyItemUseCase.DeleteItemCommand(puree.getId()));

        assertEquals(0, queryItem.findAll().size());
    }

    @Test
    void canRemoveIngredientsFromRecipe() {
        var puree = givenItemCreated("Puree", INTERMEDIATE());
        var butter = givenItemCreated("Butter", BASIC());
        var potato = givenItemCreated("Potato", BASIC());
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), potato.getId(), BigDecimal.ONE));

        Long ingredientId = queryItem.findById(puree.getId()).getRecipe().getIngredients().stream().findFirst().get().getId();
        modifyItem.removeIngredientFromRecipe(new RemoveIngredientFromRecipeCommand(puree.getId(), ingredientId));

        assertEquals(1, queryItem.findById(puree.getId()).getRecipe().getIngredients().size());
    }


}