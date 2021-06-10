package pl.kamil.chefscookbook.food.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.database.UserRepository;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.ITEM_NAME_TAKEN;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.ERROR;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.ITEM_CREATE;


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

        MockHttpServletRequestBuilder postRequest = post("/food/new-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("itemName", "ab")
                .param("type", BASIC.name())
                .param("unit", KILOGRAM.name())
                .with(csrf());

        mockMvc.perform(postRequest)

                .andExpect(model().attributeHasErrors("createNewItemCommand"))
                .andExpect(view().name(ITEM_CREATE));
    }
    @Test
    void ifNameAlreadyTakenShouldResultInError() throws Exception {

        MockHttpServletRequestBuilder postRequest = post("/food/new-item")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("itemName", "abc")
                .param("type", BASIC.name())
                .param("unit", KILOGRAM.name())
                .with(csrf());
        Mockito.when(modifyItem.createItem(any(), any())).thenReturn(Response.failure(ITEM_NAME_TAKEN));

        mockMvc.perform(postRequest)
                .andExpect(model().attributeExists(ERROR))
                .andExpect(view().name(ITEM_CREATE));
    }
//    @Test
//    void

}