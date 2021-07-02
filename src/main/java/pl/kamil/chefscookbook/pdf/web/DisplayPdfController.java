package pl.kamil.chefscookbook.pdf.web;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuService;
import pl.kamil.chefscookbook.pdf.application.port.PdfCreationService;
import pl.kamil.chefscookbook.shared.controller.ValidatedController;
import pl.kamil.chefscookbook.shared.response.Response;

import java.security.Principal;

@Controller
@RequestMapping("/pdf")
@AllArgsConstructor
public class DisplayPdfController extends ValidatedController<ItemDto> {

private final PdfCreationService pdfCreation;
private final QueryItemService queryItem;
private final QueryMenuService queryMenu;


    @SneakyThrows
    @GetMapping("/item")
    public ResponseEntity<Resource> getSingleRecipePdf(@RequestParam Long itemId, Principal user) {

        Response<ItemDto> queried = queryItem.findById(itemId, user);
        if (!queried.isSuccess()) return ResponseEntity.noContent().build();

        ItemDto item = queried.getData();

        Resource resource = new ByteArrayResource(pdfCreation.generatePdfForItem((RichItem) item).toByteArray());
        String contentDisposition = getContentDisposition(item.getName());

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);

    }
    @SneakyThrows
    @GetMapping("/menu")
    public ResponseEntity<Resource> getMenuPdf(@RequestParam Long menuId, Principal user) {

        Response<FullMenu> queried = queryMenu.getFullMenu(menuId, user);
        if (!queried.isSuccess()) return ResponseEntity.noContent().build();

        FullMenu menu = queried.getData();

        Resource resource = new ByteArrayResource(pdfCreation.generatePdfForMenu(menu).toByteArray());
        String contentDisposition = getContentDisposition(menu.getMenuName());

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);

    }


    private String getContentDisposition(String name) {
        return "attachment; filename=\"" + name +".pdf" + "\"";
    }
}

