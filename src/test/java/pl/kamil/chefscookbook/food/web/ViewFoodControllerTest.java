package pl.kamil.chefscookbook.food.web;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.ITEMS_LIST;

@WebMvcTest({ViewFoodController.class})
@ActiveProfiles("test")
@WithMockUser
class ViewFoodControllerTest {


    @MockBean
    QueryItemService queryItem;

    @Autowired
    MockMvc mockMvc;


    @Test
    void gettingMyItemsPageShouldPopulateModelWithCommandsAndItems() throws Exception {
        mockMvc.perform(get("/food/my-items"))

                .andExpect(model().attributeExists("queryItemCommand"))
                .andExpect(model().attributeExists("deleteItemCommand"))
                .andExpect(view().name(ITEMS_LIST));
        verify(queryItem, times(1)).findAllItemsBelongingToUser(any());
    }

    @SneakyThrows
    @Test
    void queryingItemForNegativeAmountShouldResultInValidationError() throws Exception {
        MockHttpServletRequestBuilder request = getGetRequestForViewItem(new BigDecimal(-1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(new RichItem()));

        mockMvc.perform(request)

                .andExpect(model().attributeHasErrors("queryItemWithDependenciesCommand"));
    }

    @SneakyThrows
    @Test
    void queryIngItemForAmountWithOverFourDecimalPlacesShouldResultInValidationError() throws Exception {
        MockHttpServletRequestBuilder request = getGetRequestForViewItem(new BigDecimal("1.234567"));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(new RichItem()));

        mockMvc.perform(request)

                .andExpect(model().attributeHasErrors("queryItemWithDependenciesCommand"));
    }

    @SneakyThrows
    @Test
    void queryingItemForInvalidAmountShouldResetAmountToOne() throws Exception {
        MockHttpServletRequestBuilder request = getGetRequestForViewItem(new BigDecimal(-1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(new RichItem()));


        mockMvc.perform(request)

                .andExpect(model().attribute("targetAmount", BigDecimal.ONE));
    }
    @SneakyThrows
    @Test
    void whenGettingViewItemDependencyMapsShouldBeSplitCorrectly() throws Exception {
        Map<ItemDto, BigDecimal> returnedMap = getMapWithOneBasicAndTwoIntermediates();
        MockHttpServletRequestBuilder request = getGetRequestForViewItem(new BigDecimal(-1));
        when(queryItem.findById(any(), any())).thenReturn(Response.success(new RichItem()));
        when(queryItem.getMapOfAllDependencies(any())).thenReturn(returnedMap);

        mockMvc.perform(request)
                .andExpect(model().attribute("basics", aMapWithSize(1)))
                .andExpect(model().attribute("intermediates", aMapWithSize(2)));


    }

    private Map<ItemDto, BigDecimal> getMapWithOneBasicAndTwoIntermediates() {
        Map<ItemDto, BigDecimal> returnedFromService = new HashMap<>();
        returnedFromService.put(new PoorItem(new Item("aa", KILOGRAM, BASIC, new UserEntity())), BigDecimal.ONE);
        returnedFromService.put(new RichItem(new Item("aa", KILOGRAM, INTERMEDIATE, new UserEntity())), BigDecimal.ONE);
        returnedFromService.put(new RichItem(new Item("aa", KILOGRAM, INTERMEDIATE, new UserEntity())), BigDecimal.ONE);
        return returnedFromService;
    }


    private MockHttpServletRequestBuilder getGetRequestForViewItem(BigDecimal amount) {
        return get("/food/view-item")
                .queryParam("itemId", String.valueOf(1))
                .queryParam("targetAmount", String.valueOf(amount))
                .with(csrf());
    }


}



















