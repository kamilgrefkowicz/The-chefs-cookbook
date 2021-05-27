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
import pl.kamil.chefscookbook.pdf.application.PdfFormattingService;
import pl.kamil.chefscookbook.shared.response.Response;

import java.security.Principal;

@Controller
@RequestMapping("/pdf")
@AllArgsConstructor
public class DisplayPdfController  {

private final PdfFormattingService pdfFormattingService;
private final QueryItemUseCase queryItem;


    @GetMapping("/item")
    public ResponseEntity<Resource> getUploadFile(@RequestParam Long itemId, Principal user) {

        Response<RichItem> queried = queryItem.findById(itemId, user);
        if (!queried.isSuccess()) return ResponseEntity.noContent().build();

        RichItem item = queried.getData();

        Resource resource = new ByteArrayResource(pdfFormattingService.generatePdfForItem(item).toByteArray());
        String contentDisposition = getContentDisposition(item);

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .contentType(MediaType.parseMediaType("application/pdf"))
                .body(resource);


    }

    private String getContentDisposition(RichItem item) {
        return "attachment; filename=\"" + item.getName() +".pdf" + "\"";
    }
}

