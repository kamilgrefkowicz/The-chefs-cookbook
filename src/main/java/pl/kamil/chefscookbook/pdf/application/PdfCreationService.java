package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.kernel.geom.Line;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.pdf.application.port.GenerateRecipePageUseCase;
import pl.kamil.chefscookbook.pdf.application.port.PdfCreationUseCase;

import java.io.*;

@Component
@AllArgsConstructor
public class PdfCreationService implements PdfCreationUseCase {

    private final GenerateRecipePageUseCase generateRecipePage;

    public ByteArrayOutputStream generatePdfForItem(RichItem item) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Document document = new Document(new PdfDocument( new PdfWriter(output)));

        generateRecipePage.execute(document, item);


        document.close();


        return output;

    }

}
