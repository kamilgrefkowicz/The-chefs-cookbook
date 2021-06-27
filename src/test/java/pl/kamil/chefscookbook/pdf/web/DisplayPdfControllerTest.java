package pl.kamil.chefscookbook.pdf.web;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuService;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.pdf.application.port.PdfCreationService;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.io.ByteArrayOutputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;

@WebMvcTest({DisplayPdfController.class})
@ActiveProfiles("test")
@WithMockUser
class DisplayPdfControllerTest {

    @MockBean
    PdfCreationService pdfCreation;
    @MockBean
    QueryItemService queryItem;
    @MockBean
    QueryMenuService queryMenu;

    @Autowired
    MockMvc mockMvc;


    @SneakyThrows
    @Test
    void ifQueryUnsuccessfulSingleRecipePdfShouldReturnNoContent() throws Exception {
        when(queryItem.findById(any(), any())).thenReturn(Response.failure(""));

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf/item")
        .queryParam("itemId", String.valueOf(1L)))

                .andExpect(status().isNoContent());
    }
    @SneakyThrows
    @Test
    void successfulQueryShouldForwardItemToPdfService() throws Exception {
        RichItem item = getRichItem();
        when(queryItem.findById(any(), any())).thenReturn(Response.success(item));
        when(pdfCreation.generatePdfForItem(item)).thenReturn(new ByteArrayOutputStream());

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf/item")
                .queryParam("itemId", String.valueOf(1L)));

        verify(pdfCreation).generatePdfForItem(item);
    }
    @SneakyThrows
    @Test
    void successfulQueryForSingleRecipeShouldPackResponseCorrectly() throws Exception {
        ByteArrayOutputStream returned = new ByteArrayOutputStream();
        RichItem item = getRichItem();
        when(queryItem.findById(any(), any())).thenReturn(Response.success(item));
        when(pdfCreation.generatePdfForItem(any())).thenReturn(returned);
        byte[] expected = returned.toByteArray();

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf/item")
                .queryParam("itemId", String.valueOf(1L)))

                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + item.getName() +".pdf" + "\""))
                .andExpect(content().bytes(expected));
    }
    @Test
    void ifQueryUnsuccessfulMenuPdfShouldReturnNoContent() throws Exception {
        when(queryMenu.getFullMenu(any(), any())).thenReturn(Response.failure(""));

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf/menu")
                .queryParam("menuId", String.valueOf(1L)))

                .andExpect(status().isNoContent());
    }
    @Test
    void successfulQueryShouldForwardMenuToPdfService() throws Exception {
        FullMenu menu = getFullMenu();
        when(queryMenu.getFullMenu(any(), any())).thenReturn(Response.success(menu));
        when(pdfCreation.generatePdfForMenu(menu)).thenReturn(new ByteArrayOutputStream());

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf/menu")
                .queryParam("menuId", String.valueOf(1L)));

        verify(pdfCreation).generatePdfForMenu(menu);
    }
    @Test
    void successfulQueryForMenuShouldPackResponseCorrectly() throws Exception {
        ByteArrayOutputStream returned = new ByteArrayOutputStream();
        FullMenu menu = getFullMenu();
        when(queryMenu.getFullMenu(any(), any())).thenReturn(Response.success(menu));
        when(pdfCreation.generatePdfForMenu(any())).thenReturn(returned);
        byte[] expected = returned.toByteArray();

        mockMvc.perform(MockMvcRequestBuilders.get("/pdf/menu")
                .queryParam("menuId", String.valueOf(1L)))

                .andExpect(header().string("Content-Disposition", "attachment; filename=\"" + menu.getMenuName() +".pdf" + "\""))
                .andExpect(content().bytes(expected));
    }

    private FullMenu getFullMenu() {
        return new FullMenu(new Menu("test", new UserEntity()), null, null, null);
    }


    private RichItem getRichItem() {
        return new RichItem(new Item("test", KILOGRAM, DISH, new UserEntity()));
    }
}










