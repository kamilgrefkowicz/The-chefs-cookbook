package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.pdf.application.port.GenerateListOfBasicIngredientsUseCase;

import java.util.Set;

@Component
public class GenerateListOfBasicIngredients implements GenerateListOfBasicIngredientsUseCase {

    private final float[] columnWidths = {130F, 130F, 130F};

    @Override
    public void execute(Document document, Set<PoorItem> basics) {


        Paragraph header = getHeaderParagraph();

        Table tableOfBasics = getTableOfBasics(basics);

        document.add(header);
        document.add(tableOfBasics);
        document.add(new AreaBreak());


    }

    private Table getTableOfBasics(Set<PoorItem> basics) {
        Table tableOfBasics = new Table(columnWidths);
        basics.forEach(item -> tableOfBasics.addCell(item.getName()));
        tableOfBasics.useAllAvailableWidth();
        tableOfBasics.setStrokeColor(Color.WHITE);
        return tableOfBasics;
    }

    private Paragraph getHeaderParagraph() {
        Paragraph header = new Paragraph("Podstawowe składniki używane w menu:");
        header.setFontSize(25);
        header.setTextAlignment(TextAlignment.CENTER);
        return header;
    }
}
