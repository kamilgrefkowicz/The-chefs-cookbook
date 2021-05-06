package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.*;
import pl.kamil.chefscookbook.food.database.IngredientJpaRepository;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ModifyItemServiceTest {

    @Autowired
    ModifyItemService modifyItem;

    @Autowired
    QueryItemService queryItem;

    @Autowired
    IngredientJpaRepository ingredientJpaRepository;

    @Autowired
    ItemJpaRepository itemJpaRepository;

    @Test
    void canCreateItem() {
        givenItemCreated("Butter", BASIC(), KILOGRAM());

        RichItem item = queryItem.findById(1L);

        assertEquals("Butter", item.getName());
        assertEquals(BASIC(), item.getType());
        assertNull(item.getRecipe());
    }

    @Test
    void creatingIntermediateOrDishItemShouldGenerateEmptyRecipe() {
        givenItemCreated("Dish", DISH(), KILOGRAM());
        givenItemCreated("Intermediate", INTERMEDIATE(), KILOGRAM());

        RichItem intermediate = queryItem.findById(1L);
        RichItem dish = queryItem.findById(2L);

        assertNotNull(intermediate.getRecipe());
        assertNotNull(dish.getRecipe());
    }

    @Test
    void canAddIngredientToRecipe() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());
        var butter = givenItemCreated("Butter", BASIC(), KILOGRAM());

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        var queried = queryItem.findById(puree.getId());

        assertFalse(queried.getRecipe().getIngredients().isEmpty());
    }




    @Test
    void addingSameIngredientToRecipeShouldAggregateAmounts() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());
        var butter = givenItemCreated("Butter", BASIC(), KILOGRAM());
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        var queried = queryItem.findById(puree.getId());

        assertEquals(1, queried.getRecipe().getIngredients().size());
        assertEquals("2.00", queried.getRecipe().getIngredients().stream().findFirst().get().getAmount().toPlainString());
    }

    @Test
    void attemptingToFormALoopShouldThrowAnException() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());
        AddIngredientCommand command = new AddIngredientCommand(puree.getId(), puree.getId(), BigDecimal.ONE);

        assertThrows(IllegalArgumentException.class, () -> modifyItem.addIngredientToRecipe(command));

    }


    @Test
    void canUpdateDescription() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());

        modifyItem.updateDescription(new UpdateDescriptionCommand(puree.getId(), "description"));
        var queried = queryItem.findById(puree.getId());

        assertEquals("description", queried.getRecipe().getDescription());
    }

    @Test
    void canDeleteItem() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());

        modifyItem.deleteItem(new ModifyItemUseCase.DeleteItemCommand(puree.getId()));

        assertEquals(0, queryItem.findAll().size());
    }
    @Test
    void deletingAnItemShouldRemoveItFromAllRecipes() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());
        var butter = givenItemCreated("Butter", BASIC(), KILOGRAM());
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        setYieldForItem(puree, BigDecimal.ONE);

        modifyItem.deleteItem(new DeleteItemCommand(butter.getId()));
        var queried = queryItem.findById(puree.getId());

        assertFalse(queried.isActive());
    }

    @Test
    void canRemoveIngredientsFromRecipe() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());
        var butter = givenItemCreated("Butter", BASIC(), KILOGRAM());
        var potato = givenItemCreated("Potato", BASIC(), KILOGRAM());
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), potato.getId(), BigDecimal.ONE));

        Long ingredientId = queryItem.findById(puree.getId()).getRecipe().getIngredients().stream().findFirst().get().getId();
        modifyItem.removeIngredientFromRecipe(new RemoveIngredientFromRecipeCommand(puree.getId(), ingredientId));

        assertEquals(1, queryItem.findById(puree.getId()).getRecipe().getIngredients().size());
    }

    @Test
    void removingLastIngredientShouldSetItemToInactive() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());
        var butter = givenItemCreated("Butter", BASIC(), KILOGRAM());
        setYieldForItem(puree, BigDecimal.valueOf(1));
        modifyItem.addIngredientToRecipe(new AddIngredientCommand(puree.getId(), butter.getId(), BigDecimal.ONE));
        Long ingredientId = queryItem.findById(puree.getId()).getRecipe().getIngredients().stream().findFirst().get().getId();

        modifyItem.removeIngredientFromRecipe(new RemoveIngredientFromRecipeCommand(puree.getId(), ingredientId));
        var queried = queryItem.findById(puree.getId());

        assertFalse(queried.isActive());
    }



    private RichItem setYieldForItem(RichItem puree, BigDecimal itemYield) {
        return modifyItem.setYield(new SetYieldCommand(puree.getId(), itemYield));
    }




    private RichItem givenItemCreated(String itemName, Type itemType, Unit itemUnit) {
        return modifyItem.createItem(new CreateNewItemCommand(itemName, itemType, itemUnit));
    }


}