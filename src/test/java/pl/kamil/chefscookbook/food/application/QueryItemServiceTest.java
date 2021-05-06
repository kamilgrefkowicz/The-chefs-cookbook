package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.SetYieldCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.GetFullItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.PoorItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.PoorItem.toPoorItem;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class QueryItemServiceTest {

    @Autowired
    QueryItemService queryItem;

    @Autowired
    ModifyItemService modifyItem;

    @Autowired
    ItemJpaRepository itemRepository;

    @Test
    void attemptingToGetFullItemInfoOnInactiveItemShouldThrowAnException() {
        var puree = givenItemCreated("Puree", INTERMEDIATE(), KILOGRAM());

        assertThrows(IllegalArgumentException.class, ()-> queryItem.getFullItem(new GetFullItemCommand(puree.getId())));
    }

    @Test
    void gettingFullItemShouldProvideACorrectMapOfItsDependencies () {
        givenSteakDishAndDependencies();
        PoorItem potato = toPoorItem(itemRepository.findFirstByNameContaining("potato"));
        PoorItem butter = toPoorItem(itemRepository.findFirstByNameContaining("butter"));
        PoorItem ribeye = toPoorItem(itemRepository.findFirstByNameContaining("ribeye steak"));
        PoorItem puree = toPoorItem(itemRepository.findFirstByNameContaining("puree"));
        PoorItem ribeyeWithPuree = toPoorItem(itemRepository.findFirstByNameContaining("ribeye with"));

        Map<PoorItem, BigDecimal> map = queryItem.getFullItem(new GetFullItemCommand(ribeyeWithPuree.getId())).getDependencyMapWithAmounts();

        assertEquals(4, map.size());
        assertEquals("0.3", map.get(ribeye).toPlainString());
        assertEquals("0.4", map.get(puree).toPlainString());
        assertEquals("0.1727", map.get(butter).toPlainString());
        assertEquals("1", map.get(ribeyeWithPuree).toPlainString());
        assertEquals("0.3636", map.get(potato).toPlainString());

    }

    private void setYieldForRecipe(RichItem item, BigDecimal yield) {
        modifyItem.setYield(new SetYieldCommand(item.getId(), yield));
    }

    private void addIngredientToRecipe(RichItem childItem, RichItem parentItem, BigDecimal amount) {
        AddIngredientCommand command = new AddIngredientCommand(parentItem.getId(), childItem.getId(), amount);
        modifyItem.addIngredientToRecipe(command);
    }


    private RichItem givenItemCreated(String itemName, Type itemType, Unit itemUnit) {
        return modifyItem.createItem(new CreateNewItemCommand(itemName, itemType, itemUnit));
    }

    private void givenSteakDishAndDependencies() {
        var potato = givenItemCreated("potato", BASIC(), KILOGRAM());
        var butter = givenItemCreated("butter", BASIC(), KILOGRAM());
        var ribeyeSteak = givenItemCreated("ribeye steak", BASIC(), KILOGRAM());
        var puree = givenItemCreated("potato puree", INTERMEDIATE(), KILOGRAM());
        var ribeyeWithButterAndPuree = givenItemCreated("ribeye with butter and puree", DISH(), PIECE());
        setYieldForRecipe(puree, BigDecimal.valueOf(1.1));
        setYieldForRecipe(ribeyeWithButterAndPuree, BigDecimal.ONE);
        addIngredientToRecipe(potato, puree, BigDecimal.valueOf(1));
        addIngredientToRecipe(butter, puree, BigDecimal.valueOf(0.2));
        addIngredientToRecipe(butter, ribeyeWithButterAndPuree, BigDecimal.valueOf(0.1));
        addIngredientToRecipe(puree, ribeyeWithButterAndPuree, BigDecimal.valueOf(0.4));
        addIngredientToRecipe(butter, ribeyeWithButterAndPuree, BigDecimal.valueOf(0.3));
    }
}