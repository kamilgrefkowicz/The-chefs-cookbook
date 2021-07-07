package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.layout.element.Div;
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
    public Div execute(Set<PoorItem> basics) {

        Div template = new Div();
        template.setKeepTogether(true);

        Paragraph header = getHeaderParagraph();

        Table tableOfBasics = getTableOfBasics(basics);

        template.add(header);
        template.add(tableOfBasics);

        return template;
    }

    private Table getTableOfBasics(Set<PoorItem> basics) {
        Table tableOfBasics = new Table(columnWidths);
        basics.forEach(item -> tableOfBasics.addCell(item.getName()));
        tableOfBasics.useAllAvailableWidth();
        return tableOfBasics;
    }

    private Paragraph getHeaderParagraph() {
        Paragraph header = new Paragraph("Podstawowe składniki używane w menu:");
        header.setFontSize(25);
        header.setTextAlignment(TextAlignment.CENTER);
        return header;
    }
}
