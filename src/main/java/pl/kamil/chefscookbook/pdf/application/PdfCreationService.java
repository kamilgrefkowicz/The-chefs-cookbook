package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.pdf.application.port.*;


import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PdfCreationService implements PdfCreationUseCase {

    private final GenerateRecipeContentUseCase generateRecipeContent;
    private final GenerateTitlePageUseCase generateTitlePage;
    private final GenerateListOfBasicIngredientsUseCase generateListOfBasicIngredients;
    private final GenerateTableOfContentsUseCase generateTableOfContents;

    protected PdfFont font;
    protected PdfFormXObject template;


    @SneakyThrows
    public ByteArrayOutputStream generatePdfForItem(RichItem item) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Document document = new Document(new PdfDocument(new PdfWriter(output)));

        font = PdfFontFactory.createFont(FontConstants.HELVETICA, PdfEncodings.CP1250, true);

        document.setFont(font);
        generateRecipeContent.execute(document, item);


        document.close();


        return output;

    }

    @SneakyThrows
    @Override
    public ByteArrayOutputStream generatePdfForMenu(FullMenu menu) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(output));

        font = PdfFontFactory.createFont(FontConstants.HELVETICA, PdfEncodings.CP1250, true);
        template = new PdfFormXObject(new Rectangle(795, 575, 30, 30));
        pdfDocument.setTagged();

        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new PageHandler());

        Document document = new Document(pdfDocument);


        document.setFont(font);


        Map<RichItem, Integer> tableOfContentsValues = new LinkedHashMap<>();

        generateTitlePage.execute(document, menu);

        generateListOfBasicIngredients.execute(document, menu.getBasics());

        generateRecipePages(document, tableOfContentsValues, menu.getIntermediates());
        generateRecipePages(document, tableOfContentsValues, menu.getDishes());

        generateTableOfContents.execute(document, tableOfContentsValues);

        int totalPages = document.getPdfDocument().getNumberOfPages();



        document.close();


        return output;
    }

//    private void stampPageNumbers(Document document) {
//        int totalPages = document.getPdfDocument().getNumberOfPages();
//
//        for (int i = 1; i<totalPages; i++) {
//        document.showTextAligned(new Paragraph(("str " + i)), 559, 806, i, TextAlignment.RIGHT, VerticalAlignment.TOP, 0);
//        }
//    }

    private void generateRecipePages(Document document, Map<RichItem, Integer> tableOfContents, Set<RichItem> intermediates) {
        for (RichItem item : intermediates) {
            tableOfContents.put(item, generateRecipeContent.execute(document, item));
        }
    }
    public class PageHandler implements IEventHandler {

        @Override
        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfPage page = docEvent.getPage();
            int pageNum = docEvent.getDocument().getPageNumber(page);

            PdfCanvas canvas = new PdfCanvas(page);

            canvas.beginText();
            canvas.beginMarkedContent(PdfName.Artifact);
            canvas.setFontAndSize(font, 12);
            canvas.moveText(500, 30);
            canvas.showText("Str " + pageNum);
            canvas.endText();
            canvas.stroke();
            canvas.addXObject(template, 0, 0);
            canvas.endMarkedContent();
            canvas.release();
        }
    }

}
