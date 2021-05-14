package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.annotation.DirtiesContext;
import pl.kamil.chefscookbook.food.application.dto.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.SetYieldCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static pl.kamil.chefscookbook.food.application.dto.ItemDto.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.PIECE;

@Transactional
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
    void mapOfDependenciesWithDefaultTargetShouldDisplayCorrectAmounts () {
        givenSteakDishAndDependencies();
        ItemDto potato = convertToDto(itemRepository.findFirstByNameContaining("potato"));
        ItemDto butter = convertToDto(itemRepository.findFirstByNameContaining("butter"));
        ItemDto ribeye = convertToDto(itemRepository.findFirstByNameContaining("ribeye steak"));
        ItemDto puree =  convertToDto(itemRepository.findFirstByNameContaining("puree"));
        ItemDto ribeyeWithPuree = convertToDto(itemRepository.findFirstByNameContaining("ribeye with"));

        Map<ItemDto, BigDecimal> map = queryItem.getMapOfAllDependencies(ribeyeWithPuree.getId(), BigDecimal.ONE);

        assertEquals(5, map.size());
        assertEquals("0.300", map.get(ribeye).toPlainString());
        assertEquals("0.400", map.get(puree).toPlainString());
        assertEquals("0.173", map.get(butter).toPlainString());
        assertEquals("1.000", map.get(ribeyeWithPuree).toPlainString());
        assertEquals("0.364", map.get(potato).toPlainString());

    }
    @Test
    void mapOfDependenciesWithMultipliedTargetShouldDisplayCorrectAmounts() {
        givenSteakDishAndDependencies();
        ItemDto potato = convertToDto(itemRepository.findFirstByNameContaining("potato"));
        ItemDto butter = convertToDto(itemRepository.findFirstByNameContaining("butter"));
        ItemDto ribeye = convertToDto(itemRepository.findFirstByNameContaining("ribeye steak"));
        ItemDto puree =  convertToDto(itemRepository.findFirstByNameContaining("puree"));
        ItemDto ribeyeWithPuree = convertToDto(itemRepository.findFirstByNameContaining("ribeye with"));

        Map<ItemDto, BigDecimal> map = queryItem.getMapOfAllDependencies(ribeyeWithPuree.getId(), BigDecimal.valueOf(4));

        assertEquals(5, map.size());
        assertEquals("1.200", map.get(ribeye).toPlainString());
        assertEquals("1.600", map.get(puree).toPlainString());
        assertEquals("0.691", map.get(butter).toPlainString());
        assertEquals("4.000", map.get(ribeyeWithPuree).toPlainString());
        assertEquals("1.455", map.get(potato).toPlainString());

    }






    private void setYieldForRecipe(PoorItem item, BigDecimal yield) {
        modifyItem.setYield(new SetYieldCommand(item.getId(), yield));
    }

    private void addIngredientToRecipe(PoorItem childItem, PoorItem parentItem, BigDecimal amount) {
        AddIngredientCommand command = new AddIngredientCommand(parentItem.getId(), childItem.getId(), amount);
        modifyItem.addIngredientToRecipe(command);
    }


    private PoorItem givenItemCreated(String itemName, Type itemType, Unit itemUnit) {
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
        addIngredientToRecipe(ribeyeSteak, ribeyeWithButterAndPuree, BigDecimal.valueOf(0.3));
    }
}