package pl.kamil.chefscookbook.menu.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuService;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.*;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.*;

@WebMvcTest({MenuController.class})
@ActiveProfiles("test")
@WithMockUser
class MenuControllerTest {

    @MockBean
    ModifyMenuService modifyMenu;
    @MockBean
    QueryMenuService queryMenu;
    @MockBean
    QueryItemService queryItem;

    @Autowired
    MockMvc mockMvc;

    @Test
    void showMyMenusShouldPopulateModelWithCommandsAndMenuList() throws Exception {
        mockMvc.perform(get("/menu/my-menus"))
                .andExpect(model().attributeExists("queryItemCommand", "createNewMenuCommand",
                        "addItemsCommand", "removeItemFromMenuCommand", "deleteMenuCommand"))
                .andExpect(model().attribute("menuList", Collections.emptyList()))
                .andExpect(view().name(MENU_LIST));

        verify(queryMenu).getAllMenusBelongingToUser(any());
    }

    @Test
    void showNewMenuFormShouldReturnCorrectView() throws Exception {
        mockMvc.perform(get("/menu/new-menu"))
                .andExpect(view().name(MENU_CREATE));
    }

    @Test
    void creatingNewMenuWithInvalidNameShouldReturnAValidationError() throws Exception {
        mockMvc.perform(getPostRequestForCreateMenu(""))
                .andExpect(model().attributeHasErrors("createNewMenuCommand"))
                .andExpect(view().name(MENU_CREATE));
    }

    @Test
    void creatingMenuWithValidNameShouldCallModifyMenuService() throws Exception {
        when(modifyMenu.createNewMenu(any(), any())).thenReturn(Response.failure(""));
        CreateNewMenuCommand expected = new CreateNewMenuCommand("test menu");

        mockMvc.perform(getPostRequestForCreateMenu("test menu"));

        verify(modifyMenu).createNewMenu(eq(expected), any());
    }

    @Test
    void creatingMenuWithNameAlreadyPresentShouldReturnError() throws Exception {
        when(modifyMenu.createNewMenu(any(), any())).thenReturn(Response.failure("test error"));

        mockMvc.perform(getPostRequestForCreateMenu("test menu"))

                .andExpect(model().attribute("error", "test error"))
                .andExpect(view().name(MENU_CREATE));
    }

    @Test
    void creatingValidMenuShouldRedirectBack() throws Exception {
        RichMenu expected = getRichMenu();
        when(modifyMenu.createNewMenu(any(), any())).thenReturn(Response.success(expected));

        mockMvc.perform(getPostRequestForCreateMenu("test menu"))

                .andExpect(model().attribute("object", expected))
                .andExpect(view().name(MENU_VIEW));
    }

    @Test
    void viewMenuRequestShouldQueryService() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.failure(""));

        mockMvc.perform(get("/menu/view-menu")
                .queryParam("menuId", String.valueOf(1L)));

        verify(queryMenu).findById(eq(1L), any());
    }

    @Test
    void viewMenuWithUnsuccessfulQueryShouldReturnError() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.failure("test error"));

        mockMvc.perform(get("/menu/view-menu")
                .queryParam("menuId", String.valueOf(1L)))

                .andExpect(model().attribute("error", "test error"))
                .andExpect(model().attributeDoesNotExist("object"))
                .andExpect(view().name(ERROR));
    }

    @Test
    void viewMenuWithSuccessfulQueryShouldReturnCorrectMaV() throws Exception {
        RichMenu returned = getRichMenu();
        when(queryMenu.findById(any(), any())).thenReturn(Response.success(returned));

        mockMvc.perform(get("/menu/view-menu")
                .queryParam("menuId", String.valueOf(1L)))

                .andExpect(model().attribute("object", returned))
                .andExpect(view().name(MENU_VIEW));
    }

    @Test
    void gettingAddItemsPageShouldQueryItemServiceForEligibleDishes() throws Exception {
        mockMvc.perform(get("/menu/add-items")
                .queryParam("menuId", String.valueOf(1L)));

        verify(queryItem).findAllEligibleDishesForMenu(any(), eq(1L));
    }

    @Test
    void gettingAddItemsPageShouldPopulateModelWithQueriedDishes() throws Exception {
        mockMvc.perform(get("/menu/add-items")
                .queryParam("menuId", String.valueOf(1L)))

                .andExpect(model().attribute("dishes", Collections.emptyList()))
                .andExpect(model().attribute("menuId", 1L))
                .andExpect(view().name(MENU_ADD_ITEMS));
    }

    @Test
        // the following few tests check security logic
        // technically what they test is impossible from inside the ui
        // repeated security logic is not tested elsewhere
    void shenanigansWithMenuIdWhileAddingItemsShouldReturnAnError() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.failure("test error"));

        mockMvc.perform(getPostRequestForAddItems(1L, new Long[]{1L}))

                .andExpect(model().attribute("error", "test error"))
                .andExpect(view().name(ERROR));
    }

    @Test
    void shenanigansWithItemIdsWhileAddingItemsShouldReturnAnError() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.success(getRichMenu()));
        when(modifyMenu.addItemsToMenu(any(), any())).thenReturn(Response.failure("test error"));

        mockMvc.perform(getPostRequestForAddItems(1L, new Long[]{1L}))

                .andExpect(model().attribute("error", "test error"))
                .andExpect(view().name(MENU_VIEW));
    }
    @Test
    void successfulAddItemsShouldReturnCorrectMaV() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.success(getRichMenu()));
        Response<RichMenu> modified =  Response.success(getRichMenu());
        RichMenu expected = modified.getData();
        when(modifyMenu.addItemsToMenu(any(), any())).thenReturn(modified);

        mockMvc.perform(getPostRequestForAddItems(1L, new Long[]{1L}))

                .andExpect(model().attribute("object", expected))
                .andExpect(view().name(MENU_VIEW));
    }
    @Test
    void removeItemFromMenuShouldCallServiceToRemove() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.success(getRichMenu()));
        when(modifyMenu.removeItemFromMenu(any(), any())).thenReturn(Response.success(getRichMenu()));
        RemoveItemFromMenuCommand expected = new RemoveItemFromMenuCommand(1L, 2L);

        mockMvc.perform(getPostRequestForRemoveItem(1L, 2L));

        verify(modifyMenu).removeItemFromMenu(eq(expected), any());
    }
    @Test
    void successfulRemoveItemShouldReturnCorrectMaV() throws Exception {
        when(queryMenu.findById(any(), any())).thenReturn(Response.success(getRichMenu()));
        Response<RichMenu> modified =  Response.success(getRichMenu());
        RichMenu expected = modified.getData();
        when(modifyMenu.removeItemFromMenu(any(), any())).thenReturn(modified);

        mockMvc.perform(getPostRequestForRemoveItem())

                .andExpect(model().attribute("object", expected))
                .andExpect(view().name(MENU_VIEW));
    }
    @Test
    void deleteMenuAttemptShouldReturnCorrectMavForConfirmation() throws Exception {
        Response<RichMenu> created =  Response.success(getRichMenu());
        RichMenu expected = created.getData();
        when(queryMenu.findById(any(), any())).thenReturn(created);

        mockMvc.perform(get("/menu/delete-menu").queryParam("menuId", String.valueOf(1L)))

                .andExpect(model().attribute("object", expected))
                .andExpect(view().name(MENU_DELETE_CONFIRM));
    }
    @Test
    void menuDeletionShouldCallServiceToDelete() throws Exception {
        DeleteMenuCommand expected = new DeleteMenuCommand(3L);

        mockMvc.perform(getPostRequestForDeleteMenu(3L));

        verify(modifyMenu).deleteMenu(eq(expected), any());
    }
    @Test
    void menuDeletionShouldRedirectBackToMenuList() throws Exception {
        List<RichMenu> expected = new ArrayList<>();
        when(queryMenu.getAllMenusBelongingToUser(any())).thenReturn(expected);

        mockMvc.perform(getPostRequestForDeleteMenu())
                .andExpect(model().attribute("menuList", expected))
                .andExpect(view().name(MENU_LIST));
    }

    private RequestBuilder getPostRequestForDeleteMenu() {
        return getPostRequestForDeleteMenu(1L);
    }

    private MockHttpServletRequestBuilder getPostRequestForDeleteMenu(Long menuId) {

        return post("/menu/delete-menu")
                .param("menuId", String.valueOf(menuId))
                .with(csrf());
    }

    private RequestBuilder getPostRequestForRemoveItem() {
        return getPostRequestForRemoveItem(1L, 1L);
    }

    private MockHttpServletRequestBuilder getPostRequestForRemoveItem(long menuId, long itemId) {
        return post("/menu/remove-item")
                .param("menuId", String.valueOf(menuId))
                .param("itemId", String.valueOf(itemId))
                .with(csrf());
    }

    private RichMenu getRichMenu() {
        Menu menu = new Menu("", getUserEntity());
        return new RichMenu(menu);
    }

    private UserEntity getUserEntity() {
        UserEntity entity = new UserEntity("", "");
        entity.setId(1L);
        return entity;
    }


    private MockHttpServletRequestBuilder getPostRequestForCreateMenu(String name) {
        return post("/menu/new-menu")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("menuName", name)
                .with(csrf());
    }

    private MockHttpServletRequestBuilder getPostRequestForAddItems(Long menuId, Long[] itemIds) {

        MockHttpServletRequestBuilder request = post("/menu/add-items")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("menuId", String.valueOf(menuId))
                .with(csrf());
        addItemsFromArray(request,"itemIds", itemIds);
        return request;
    }

    private void addItemsFromArray(MockHttpServletRequestBuilder builder,String paramName,  Long[] array) {
        Arrays.stream(array).forEach(id -> builder.param(paramName, String.valueOf(id)));
    }
}