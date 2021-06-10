package pl.kamil.chefscookbook.food.web;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.shared.response.Response;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.ITEM_NAME_TAKEN;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.*;


@WebMvcTest({ModifyFoodController.class})
@ActiveProfiles("test")
@WithMockUser
class ModifyFoodControllerTest {

    @MockBean
    ModifyItemService modifyItem;

    @MockBean
    QueryItemService queryItem;

    @Autowired
    MockMvc mockMvc;


    @Test
    void newItemRequestShouldReturnCorrectModelAndView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/food/new-item"))

                .andExpect(model().attributeExists("createNewItemCommand"))
                .andExpect(view().name(ITEM_CREATE));
    }

    @Test
    void invalidItemNameShouldResultInValidationError() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewItem("ab", BASIC, null);

        mockMvc.perform(postRequest)

                .andExpect(model().attributeHasErrors("createNewItemCommand"))
                .andExpect(view().name(ITEM_CREATE));
    }

    @Test
    void ifNameAlreadyTakenShouldResultInError() throws Exception {

        MockHttpServletRequestBuilder postRequest = getPostRequestForNewItem("abc", BASIC, null);
        when(modifyItem.createItem(any(), any())).thenReturn(Response.failure(ITEM_NAME_TAKEN));

        mockMvc.perform(postRequest)
                .andExpect(model().attributeExists(ERROR))
                .andExpect(view().name(ITEM_CREATE));
    }

    @Test
    void creatingItemShouldCallService() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewItem("abc", BASIC, null);
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(null));

        mockMvc.perform(postRequest);

        verify(modifyItem, times(1)).createItem(any(), any());
    }


    @Test
    void creatingBasicItemFromNewItemPageShouldRedirectBack() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewItem("abc", BASIC, null);
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(null));

        mockMvc.perform(postRequest)

                .andExpect(view().name(ITEM_CREATE));
    }

    @Test
    void creatingBasicItemFromModifyItemPageShouldRedirectBack() throws Exception {
        RichItem redirectedFrom = new RichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewItem("abc", BASIC, 1L);
        when(queryItem.findById(eq(1L), any())).thenReturn(Response.success(redirectedFrom));
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(null));

        mockMvc.perform(postRequest)

                .andExpect(view().name(ITEM_MODIFY))
                .andExpect(model().attribute("object", redirectedFrom));
    }

    @Test
    void creatingNonBasicItemShouldRedirectToModifyItemPage() throws Exception {
        RichItem newItem = new RichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewItem("abc", INTERMEDIATE, null);
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(newItem));

        mockMvc.perform(postRequest)

                .andExpect(view().name(ITEM_MODIFY))
                .andExpect(model().attribute("object", newItem));
    }

    //technically, this event should not be possible from inside the ui. Implemented and tested only in case of shenanigans in manipulating requests
    @Test
    void shenanigansWithAddingIngredientShouldResultInError() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForAddIngredient(BigDecimal.ONE);
        when(queryItem.findById(any(), any())).thenReturn(Response.failure("error"));

        mockMvc.perform(postRequest)

                .andExpect(view().name(ERROR))
                .andExpect(model().attributeExists("error"));
    }
    @Test
    void negativeAmountForAddIngredientShouldReturnValidationError() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForAddIngredient(new BigDecimal(-1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(new RichItem()));

        mockMvc.perform(postRequest)
                .andExpect(model().attributeHasErrors("addIngredientCommand"))
                .andExpect(view().name(ITEM_MODIFY));
        verifyNoInteractions(modifyItem);
    }
    @Test
    void unsuccessfulAddIngredientShouldReturnItemBeforeChange() throws Exception {
        RichItem beforeChange = new RichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForAddIngredient(new BigDecimal(1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.addIngredientToRecipe(any(), any())).thenReturn(Response.failure("error"));

        mockMvc.perform(postRequest)

                .andExpect(model().attribute("object", beforeChange))
                .andExpect(view().name(ITEM_MODIFY));
    }
    @Test
    void successfulAddIngredientShouldReturnChangedItem() throws Exception {
        RichItem beforeChange = new RichItem();
        RichItem afterChange = new RichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForAddIngredient(new BigDecimal(1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.addIngredientToRecipe(any(), any())).thenReturn(Response.success(afterChange));

        mockMvc.perform(postRequest)

                .andExpect(model().attribute("object", afterChange))
                .andExpect(view().name(ITEM_MODIFY));
        verify(modifyItem, times(1)).addIngredientToRecipe(any(), any());
        System.out.println(beforeChange.equals(afterChange));
    }



    @Test
    void successfulRemoveIngredientShouldReturnChangedItem() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/food/new-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("parentItemId", String.valueOf(1L))
                .param("ingredientId", String.valueOf(2L))
                .with(csrf());
        when(queryItem.findById(any(), any())).thenReturn(Response.success(new RichItem()));

        mockMvc.perform(postRequest);
    }


    private MockHttpServletRequestBuilder getPostRequestForNewItem(String itemName, Type type, Long itemId) {
        MockHttpServletRequestBuilder postRequest = post("/food/new-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("itemName", itemName)
                .param("type", type.name())
                .param("unit", KILOGRAM.name())
                .with(csrf());

        if (itemId != null) postRequest.param("itemId", String.valueOf(itemId));

        return postRequest;
    }


    private MockHttpServletRequestBuilder getPostRequestForAddIngredient(BigDecimal amount) {
        return post("/food/add-ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("parentItemId", String.valueOf(1L))
                .param("childItemId", String.valueOf(2L))
                .param("amount", String.valueOf(amount))
                .with(csrf());
    }
}
