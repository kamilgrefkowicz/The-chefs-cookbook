package pl.kamil.chefscookbook.pdf.web;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.menu.application.QueryMenu;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuUseCase;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.pdf.application.port.PdfCreationUseCase;
import pl.kamil.chefscookbook.shared.response.Response;

import java.security.Principal;

@Controller
@RequestMapping("/pdf")
@AllArgsConstructor
public class DisplayPdfController  {

private final PdfCreationUseCase pdfCreation;
private final QueryItemUseCase queryItem;
private final QueryMenuUseCase queryMenu;


    @GetMapping("/item")
    public ResponseEntity<Resource> getSingleRecipePdf(@RequestParam Long itemId, Principal user) {

        Response<RichItem> queried = queryItem.findById(itemId, user);
        if (!queried.isSuccess()) return ResponseEntity.noContent().build();

        RichItem item = queried.getData();

        Resource resource = new ByteArrayResource(pdfCreation.generatePdfForItem(item).toByteArray());
        String contentDisposition = getContentDisposition(item.getName());

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);

    }
    @GetMapping("/menu")
    public ResponseEntity<Resource> getMenuPdf(@RequestParam Long menuId, Principal user) {

        Response<MenuDto> queried = queryMenu.findById(menuId, user, true);
        if (!queried.isSuccess()) return ResponseEntity.noContent().build();

        FullMenu menu = (FullMenu) queried.getData();

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

