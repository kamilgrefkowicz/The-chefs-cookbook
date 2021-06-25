package pl.kamil.chefscookbook.food.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
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
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewBasicItem("ab", BASIC, null);

        mockMvc.perform(postRequest)

                .andExpect(model().attributeHasErrors("createNewItemCommand"))
                .andExpect(view().name(BASIC_ITEMS_VIEW));
    }

    @Test
    void ifNameAlreadyTakenShouldResultInError() throws Exception {

        MockHttpServletRequestBuilder postRequest = getPostRequestForNewBasicItem("abc", BASIC, null);
        when(modifyItem.createItem(any(), any())).thenReturn(Response.failure(ITEM_NAME_TAKEN));

        mockMvc.perform(postRequest)
                .andExpect(model().attribute(ERROR, ITEM_NAME_TAKEN))
                .andExpect(view().name(BASIC_ITEMS_VIEW));
    }

    @Test
    void creatingItemShouldCallService() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewBasicItem("abc", BASIC, null);
        PoorItem returned = getPoorItem();
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(returned));

        mockMvc.perform(postRequest);

        verify(modifyItem, times(1)).createItem(any(), any());
    }

    private PoorItem getPoorItem() {
        return new PoorItem(new Item("", KILOGRAM, BASIC, new UserEntity()));
    }


    @Test
    void creatingBasicItemFromBasicItemsListShouldRedirectBack() throws Exception {
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewBasicItem("abc", BASIC, null);
        PoorItem returned = getPoorItem();
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(returned));

        mockMvc.perform(postRequest)

                .andExpect(view().name(BASIC_ITEMS_VIEW));
    }

    @Test
    void creatingBasicItemFromModifyItemPageShouldRedirectBack() throws Exception {
        RichItem redirectedFrom = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForNewBasicItem("abc", BASIC, 1L);
        when(queryItem.findById(eq(1L), any())).thenReturn(Response.success(redirectedFrom));
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(null));

        mockMvc.perform(postRequest)

                .andExpect(view().name(ITEM_MODIFY))
                .andExpect(model().attribute("object", redirectedFrom));
    }

    @Test
    void creatingNonBasicItemShouldRedirectToModifyItemPage() throws Exception {
        RichItem newItem = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForAdvancedItem("abc", INTERMEDIATE);
        when(modifyItem.createItem(any(), any())).thenReturn(Response.success(newItem));

        mockMvc.perform(postRequest)

                .andExpect(view().name(ITEM_MODIFY))
                .andExpect(model().attribute("object", newItem));
    }

    private MockHttpServletRequestBuilder getPostRequestForAdvancedItem(String itemName, Type type) {
        return post("/food/new-advanced-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("itemName", itemName)
                .param("type", type.name())
                .param("unit", KILOGRAM.name())
                .with(csrf());
    }

    @Test
    void gettingModifyItemPageShouldReturnModelPopulatedWithCommandsNeededForForms() throws Exception {
        RichItem queried = getRichItem();
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/food/modify-item")
                .queryParam("itemId", String.valueOf(1L));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(queried));

        mockMvc.perform(getRequest)
                .andExpect(model().attributeExists("addIngredientCommand", "removeIngredientCommand", "setYieldCommand",
                        "updateDescriptionCommand", "deleteItemCommand"))
                .andExpect(model().attribute("object", queried))
                .andExpect(view().name(ITEM_MODIFY));
    }

    // technically, this event should not be possible from inside the ui.
    // implemented and tested only in case of shenanigans in manipulating requests.
    // repeated logic is not tested elsewhere
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
        when(queryItem.findById(any(), any())).thenReturn(Response.success(getRichItem()));

        mockMvc.perform(postRequest)
                .andExpect(model().attributeHasErrors("addIngredientCommand"))
                .andExpect(view().name(ITEM_MODIFY));
        verifyNoInteractions(modifyItem);
    }
    @Test
    void unsuccessfulAddIngredientShouldReturnItemBeforeChange() throws Exception {
        RichItem beforeChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForAddIngredient(new BigDecimal(1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.addIngredientToRecipe(any(), any())).thenReturn(Response.failure("error"));

        mockMvc.perform(postRequest)

                .andExpect(model().attribute("object", beforeChange))
                .andExpect(view().name(ITEM_MODIFY));
    }
    @Test
    void successfulAddIngredientShouldReturnChangedItem() throws Exception {
        RichItem beforeChange = getRichItem();
        RichItem afterChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForAddIngredient(new BigDecimal(1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.addIngredientToRecipe(any(), any())).thenReturn(Response.success(afterChange));

        mockMvc.perform(postRequest)

                .andExpect(model().attribute("object", afterChange))
                .andExpect(view().name(ITEM_MODIFY));
        verify(modifyItem, times(1)).addIngredientToRecipe(any(), any());
    }



    @Test
    void successfulRemoveIngredientShouldReturnChangedItem() throws Exception {
        RichItem beforeChange = getRichItem();
        RichItem afterChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = post("/food/remove-ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("parentItemId", String.valueOf(1L))
                .param("ingredientId", String.valueOf(2L))
                .with(csrf());
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.removeIngredientFromRecipe(any(), any())).thenReturn(Response.success(afterChange));

        mockMvc.perform(postRequest)
        .andExpect(view().name(ITEM_MODIFY))
        .andExpect(model().attribute("object", afterChange));
        verify(modifyItem, times(1)).removeIngredientFromRecipe(any(), any());
    }
    @Test
    void settingNegativeYieldShouldResultInValidationError() throws Exception {
        RichItem beforeChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForSetYield(new BigDecimal(-1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));

        mockMvc.perform(postRequest)
                .andExpect(model().attributeHasErrors("setYieldCommand"))
                .andExpect(model().attribute("object", beforeChange))
                .andExpect(view().name(ITEM_MODIFY));
        verifyNoInteractions(modifyItem);
    }
    @Test
    void settingValidYieldShouldReturnChangedItem() throws Exception {
        RichItem beforeChange = getRichItem();
        RichItem afterChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForSetYield(new BigDecimal(1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.setYield(any(), any())).thenReturn(Response.success(afterChange));

        mockMvc.perform(postRequest)
                .andExpect(model().attribute("object", afterChange))
                .andExpect(view().name(ITEM_MODIFY));
    }
    @Test
    void settingDescriptionOver1000CharactersShouldResultInValidationError() throws Exception {
        RichItem beforeChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForModifyDescription(getLongDescription());
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));

        mockMvc.perform(postRequest)

                .andExpect(model().attributeHasErrors("updateDescriptionCommand"))
                .andExpect(model().attribute("object", beforeChange))
                .andExpect(view().name(ITEM_MODIFY));
        verifyNoInteractions(modifyItem);
    }
    @Test
    void settingValidDescriptionShouldReturnChangedItem() throws Exception {
        RichItem beforeChange = getRichItem();
        RichItem afterChange = getRichItem();
        MockHttpServletRequestBuilder postRequest = getPostRequestForModifyDescription("aa");
        when(queryItem.findById(any(), any())).thenReturn(Response.success(beforeChange));
        when(modifyItem.updateDescription(any(), any())).thenReturn(Response.success(afterChange));

        mockMvc.perform(postRequest)

                .andExpect(model().attribute("object", afterChange))
                .andExpect(view().name(ITEM_MODIFY));
    }

    private RichItem getRichItem() {
        return new RichItem(new Item("", KILOGRAM, INTERMEDIATE, new UserEntity()));
    }

    @Test
    void attemptingToDeleteItemShouldReturnWarningPage() throws Exception {
        List<PoorItem> list = new ArrayList<>();
        RichItem affected = new RichItem();
        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get("/food/delete-item")
                .queryParam("itemId", String.valueOf(1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(affected));
        when(queryItem.findAllItemsAffectedByDelete(any())).thenReturn(list);

        mockMvc.perform(getRequest)
                .andExpect(model().attribute("itemsAffected", list))
                .andExpect(model().attribute("object", affected))
                .andExpect(view().name(ITEM_DELETE_CONFIRM));
    }
    @Test
    void deletingItemShouldReturnItemsList() throws Exception {
        MockHttpServletRequestBuilder postRequest = post("/food/delete-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("itemId", String.valueOf(1L))
                .with(csrf());
        when(queryItem.findAllItemsBelongingToUser(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(postRequest)
                .andExpect(model().attributeExists("poorItemList"))
                .andExpect(view().name(ITEMS_LIST));
        verify(modifyItem, times(1)).deleteItem(any(),any());
    }

    private String getLongDescription() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i< 1111; i++) {
            builder.append("a");
        }
        return builder.toString();
    }


    private MockHttpServletRequestBuilder getPostRequestForNewBasicItem(String itemName, Type type, Long itemId) {
        MockHttpServletRequestBuilder postRequest = post("/food/new-basic-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("itemName", itemName)
                .param("type", type.name())
                .param("unit", KILOGRAM.name())
                .with(csrf());

        if (itemId != null) postRequest.param("redirectItemId", String.valueOf(itemId));

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
    private MockHttpServletRequestBuilder getPostRequestForModifyDescription(String description) {
        return post("/food/modify-description")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("parentItemId", String.valueOf(1L))
                .param("description", description)
                .with(csrf());
    }
    private MockHttpServletRequestBuilder getPostRequestForSetYield(BigDecimal yield) {
        return post("/food/set-yield")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("parentItemId", String.valueOf(1L))
                .param("itemYield", String.valueOf(yield))
                .with(csrf());
    }
}
