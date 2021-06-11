package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemService.QueryItemWithDependenciesCommand;
import pl.kamil.chefscookbook.food.database.IngredientRepository;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.NOT_AUTHORIZED;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.NOT_FOUND;

@ExtendWith(MockitoExtension.class)
class QueryItemTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    UserSecurityService userSecurity;
    @Mock
    IngredientRepository ingredientRepository;
    @InjectMocks
    QueryItem queryItem;



    @Test
    void findAllItemsBelongingToUserShouldCallRepository() {
        Principal user = getUserWithIdOf1();

        queryItem.findAllItemsBelongingToUser(user);

        verify(itemRepository).findAllByUserEntityId(1L);
    }
    @Test
    void findAllItemsBelongingToUserShouldMapItemsToPoorItems() {
        Principal user = getUserWithIdOf1();
        List<Item> listOfTwoItems = getListOfTwoItems();
        when(itemRepository.findAllByUserEntityId(any())).thenReturn(listOfTwoItems);

        List<PoorItem> queried = queryItem.findAllItemsBelongingToUser(user);

        assertThat(queried, hasSize(2));
    }
    @Test
    void findForAutocompleteShouldCallRepository() {
        Principal user = getUserWithIdOf1();

        queryItem.findForAutocomplete("test", user);

        verify(itemRepository).findForAutocomplete("test", 1L);
    }
    @Test
    void findForAutocompleteShouldMapItemsToDto() {
        Principal user = getUserWithIdOf1();
        when(itemRepository.findForAutocomplete("test", 1L)).thenReturn(getListOfTwoItems());

        List<ItemAutocompleteDto> queried = queryItem.findForAutocomplete("test", user);

        assertThat(queried, hasSize(2));
    }
    @Test
    void findAllItemsAffectedByDeleteShouldCallRepository() {
        queryItem.findAllItemsAffectedByDelete(1L);

        verify(ingredientRepository).findAllByChildItemId(1L);
    }
    @Test
    void findAllItemsAffectedByDeleteShouldMapIngredientsParentToPoorItem() {
        Item parentItem = getItem(1L, DISH);
        parentItem.setName("parent");
        Item childItem = getItem(2L, BASIC);
        parentItem.addIngredient(childItem, BigDecimal.ONE);
        Ingredient ingredient = parentItem.getIngredients().stream().findFirst().get();
        when(ingredientRepository.findAllByChildItemId(1L)).thenReturn(Collections.singletonList(ingredient));

        List<PoorItem> queried = queryItem.findAllItemsAffectedByDelete(1L);

        assertThat(queried, hasSize(1));
        assertThat(queried.get(0).getName(), equalTo("parent"));
    }
    @Test
    void findAllDishesEligibleForMenuShouldCallRepository() {
        Principal user = getUserWithIdOf1();
        queryItem.findAllEligibleDishesForMenu(user, 2L);

        verify(itemRepository).findAllDishesByUser(1L);
    }
    @Test
    void findAllDishesEligibleForMenuShouldFilterOutItemsInMenuAlready() {
        Principal user = getUserWithIdOf1();
        Menu menu = new Menu("test", new UserEntity());
        menu.setId(3L);
        Item inMenu = getItem(1L, DISH);
        inMenu.setName("In menu");
        menu.addItemsToMenu(Collections.singleton(inMenu));
        inMenu.setMenus(Collections.singleton(menu));
        Item notInMenu = getItem(2L, DISH);
        notInMenu.setName("Not in menu");
        when(itemRepository.findAllDishesByUser(any())).thenReturn(List.of(inMenu, notInMenu));

        List<PoorItem> queried = queryItem.findAllEligibleDishesForMenu(user, 3L);

        assertThat(queried, hasSize(1));
        assertThat(queried.get(0).getName(), equalTo("Not in menu"));
    }
    @Test
    void findByIdShouldCallRepository() {
        Principal user = getUserWithIdOf1();
        queryItem.findById(1L, user);

        verify(itemRepository).findById(1L);
    }
    @Test
    void findByIdShouldReturnFailureIfNoItemWithId() {
        Principal user = getUserWithIdOf1();
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        Response<RichItem> queried= queryItem.findById(1L, user);

        assertFalse(queried.isSuccess());
        assertThat(queried.getError(), equalTo(NOT_FOUND));
    }
    @Test
    void findByIdShouldReturnFailureIfNotOwnerOfItem() {
        Principal user = getUserWithIdOf1();
        when(itemRepository.findById(any())).thenReturn( Optional.of(new Item()));
        when(userSecurity.belongsTo(any(), any())).thenReturn(false);

        Response<RichItem> queried= queryItem.findById(1L, user);

        assertFalse(queried.isSuccess());
        assertThat(queried.getError(), equalTo(NOT_AUTHORIZED));
    }
    @Test
    void validFindByIdQueryShouldReturnSuccessfulResponse() {
        Principal user = getUserWithIdOf1();
        when(itemRepository.findById(any())).thenReturn( Optional.of(getItem(1L, DISH)));
        when(userSecurity.belongsTo(any(), any())).thenReturn(true);

        Response<RichItem> queried= queryItem.findById(1L, user);

        assertTrue(queried.isSuccess());
        assertThat(queried.getData().getId(), equalTo(1L));
    }
    @Test
    void gettingMapOfDependenciesShouldCallRepository() {
        when(itemRepository.getOne(any())).thenReturn(getItem(1L, DISH));

        queryItem.getMapOfAllDependencies(getQueryItemWithDependenciesCommand());

        verify(itemRepository).getOne(any());
    }

    private QueryItemWithDependenciesCommand getQueryItemWithDependenciesCommand() {
        return new QueryItemWithDependenciesCommand(1L, BigDecimal.ONE);
    }


    private List<Item> getListOfTwoItems() {
        List<Item> items = new ArrayList<>();
        Item item1 = getItem(1L);
        items.add(item1);

        Item item2 = getItem(2L);
        items.add(item2);

        return items;
    }

    private Item getItem(Long id) {
        Item item = new Item("aa", KILOGRAM, BASIC, new UserEntity());
        item.setId(id);
        return item;
    }
    private Item getItem(Long id, Type type) {
        Item item = new Item("aa", KILOGRAM, type, new UserEntity());
        item.setId(id);
        return item;
    }

    private Principal getUserWithIdOf1() {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(1L);
            }
        };
    }

}