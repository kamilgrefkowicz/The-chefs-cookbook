package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.pdf.application.port.*;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PdfCreation implements PdfCreationService {

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
        document.add(generateRecipeContent.execute(item));


        document.close();


        return output;

    }

    @SneakyThrows
    @Override
    public ByteArrayOutputStream generatePdfForMenu(FullMenu menu) {

        font = PdfFontFactory.createFont(FontConstants.HELVETICA, PdfEncodings.CP1250, true);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(output));
        Document document = new Document(pdfDocument);

        addEventHandlerForPageNumbering(pdfDocument);
        document.setFont(font);

        Map<RichItem, Integer> tableOfContentsValues = new LinkedHashMap<>();

        document.add(generateTitlePage.execute(menu));

        generateRecipePages(document, tableOfContentsValues, menu.getIntermediates());
        generateRecipePages(document, tableOfContentsValues, menu.getDishes());

        document.add(generateListOfBasicIngredients.execute(menu.getBasics()));

        document.add(generateTableOfContents.execute(tableOfContentsValues));

        document.close();

        return output;
    }

    private void addEventHandlerForPageNumbering(PdfDocument pdfDocument) {
        template = new PdfFormXObject(new Rectangle(795, 575, 30, 30));
        pdfDocument.setTagged();
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new PageHandler());
    }


    private void generateRecipePages(Document document, Map<RichItem, Integer> tableOfContents, Set<RichItem> items) {
        for (RichItem item : items) {
            tableOfContents.put(item, document.getPdfDocument().getNumberOfPages());
            document.add(generateRecipeContent.execute(item));
        }
        document.add(new AreaBreak());
    }

     class PageHandler implements IEventHandler {

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
