package pl.kamil.chefscookbook.menu.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuService;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
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
                .andExpect(model().attributeExists("queryItemCommand","createNewMenuCommand",
                        "addItemsCommand","removeItemFromMenuCommand" ,"deleteMenuCommand"))
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
        when(modifyMenu.createNewMenu(any(), any())).thenReturn(Response.failure("test error"));

        mockMvc.perform(getPostRequestForCreateMenu("test menu"));

        verify(modifyMenu).createNewMenu(any(), any());
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
        RichMenu returned = getRichMenu();
        when(modifyMenu.createNewMenu(any(), any())).thenReturn(Response.success(returned));

        mockMvc.perform(getPostRequestForCreateMenu("test menu"))

                .andExpect(model().attribute("object", returned))
                .andExpect(view().name(MENU_VIEW));
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
}