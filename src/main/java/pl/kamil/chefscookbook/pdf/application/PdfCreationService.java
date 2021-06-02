package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Line;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.pdf.application.port.GenerateRecipePageUseCase;
import pl.kamil.chefscookbook.pdf.application.port.PdfCreationUseCase;


import java.io.*;

import static com.itextpdf.kernel.pdf.PdfName.*;

@Component
@AllArgsConstructor
public class PdfCreationService implements PdfCreationUseCase {

    private final GenerateRecipePageUseCase generateRecipePage;

    @SneakyThrows
    public ByteArrayOutputStream generatePdfForItem(RichItem item) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Document document = new Document(new PdfDocument(new PdfWriter(output)));

        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA, PdfEncodings.CP1250, true);

        document.setFont(font);
        generateRecipePage.execute(document, item);

        document.showTextAligned(new Paragraph(String.format("page %s of %s", 1, 1)), 559, 806, 1, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);



        document.close();


        return output;

    }

    @Override
    public ByteArrayOutputStream generatePdfForMenu(FullMenu menu) {
        return null;
    }

}
