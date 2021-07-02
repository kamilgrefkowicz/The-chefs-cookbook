package pl.kamil.chefscookbook.menu.application;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;

@ExtendWith(MockitoExtension.class)
class QueryMenuTest {

    @Mock
    MenuRepository menuRepository;
    @Mock
    UserSecurityService userSecurity;

    @InjectMocks
    QueryMenu queryMenu;


    @Test
    void getAllMenusByUserShouldQueryMenuRepository() {
        Principal principal = getPrincipalWithIdOf1();

        queryMenu.getAllMenusBelongingToUser(principal);

        verify(menuRepository).findAllByUserEntityId(1L);
    }
    @Test
    void getAllMenusShouldReturnAListOfMappedMenus() {
        Principal principal = getPrincipalWithIdOf1();
        List<Menu> returned = new ArrayList<>(List.of(getMenu(1L), getMenu(2L)));
        when(menuRepository.findAllByUserEntityId(1L)).thenReturn(returned);

        List<RichMenu> menus = queryMenu.getAllMenusBelongingToUser(principal);

        assertThat(menus, hasSize(2));
        assertThat(menus.get(0).getMenuId(), equalTo(1L));
        assertThat(menus.get(1).getMenuId(), equalTo(2L));
    }
    @SneakyThrows
    @Test
    void findMenuByIdShouldCallRepositoryToFind() {
        Principal principal = getPrincipalWithIdOf1();
        when(menuRepository.findById(any())).thenReturn(Optional.of(getMenu()));
        passSecurity();

        queryMenu.findById(1L, principal);

        verify(menuRepository).findById(1L);
    }
    @SneakyThrows
    @Test
    void validFindMenuQueryShouldReturnCorrectResponse() {
        Principal principal = getPrincipalWithIdOf1();
        passSecurity();
        Menu returned = getMenu(3L);
        returned.setName("test");
        when(menuRepository.findById(any())).thenReturn(Optional.of(returned));


        Response<RichMenu> response = queryMenu.findById(1L, principal);
        RichMenu data = response.getData();

        assertTrue(response.isSuccess());
        assertThat(data.getMenuName(), equalTo("test"));
        assertThat(data.getMenuId(), equalTo(3L));
    }
    @SneakyThrows
    @Test
    void gettingFullMenuOnEmptyMenuShouldReturnThreeEmptySets() {
        Principal principal = getPrincipalWithIdOf1();
        when(menuRepository.findById(any())).thenReturn(Optional.of(getMenu()));
        passSecurity();

        Response<FullMenu> response = queryMenu.getFullMenu(1L, principal);
        FullMenu data = response.getData();

        assertTrue(response.isSuccess());
        assertThat(data.getBasics(), hasSize(0));
        assertThat(data.getIntermediates(), hasSize(0));
        assertThat(data.getDishes(), hasSize(0));
    }
    @SneakyThrows
    @Test
    void gettingFullMenuShouldCorrectlyMapAllItems() {
        Menu menu = getComplexMenuWithTwoOfEachType();
        Principal principal = getPrincipalWithIdOf1();
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));
        passSecurity();

        Response<FullMenu> response = queryMenu.getFullMenu(1L, principal);
        FullMenu data = response.getData();

        assertThat(data.getDishes(), hasSize(2));
        assertThat(data.getIntermediates(), hasSize(2));
        assertThat(data.getBasics(), hasSize(2));
    }

    private Menu getComplexMenuWithTwoOfEachType() {
        Menu menu = getMenu();
        Item dish1 = getItem(DISH);
        Item dish2 = getItem(DISH);
        Item intermediate1 = getItem(INTERMEDIATE);
        Item intermediate2 = getItem(INTERMEDIATE);
        Item basic1 = getItem(BASIC);
        Item basic2 = getItem(BASIC);
        dish1.addIngredient(intermediate1, BigDecimal.ONE);
        dish1.addIngredient(intermediate2, BigDecimal.ONE);
        dish1.addIngredient(basic1, BigDecimal.ONE);
        intermediate1.addIngredient(basic2, BigDecimal.ONE);
        intermediate1.addIngredient(basic1, BigDecimal.ONE);

        menu.addItemsToMenu(Set.of(dish1, dish2, intermediate1, intermediate2, basic1, basic2));

        return menu;
    }

    private Item getItem(Type type) {
        return new Item("", KILOGRAM, type, new UserEntity());
    }

    private Menu getMenu(Long id) {
        Menu menu = new Menu("", new UserEntity());
        menu.setId(id);
        return menu;
    }

    private void passSecurity() {
        when(userSecurity.belongsTo(any(), any())).thenReturn(true);
    }

    private Menu getMenu() {
        return getMenu(1L);
    }


    private Principal getPrincipalWithIdOf1() {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(1L);
            }
        };
    }
}















