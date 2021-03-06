package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.pdf.application.port.GenerateTableOfContentsUseCase;

import java.util.Map;

@Component
public class GenerateTableOfContents implements GenerateTableOfContentsUseCase {

    private final float[] columnWidths = {100F, 30F, 100F, 30F, 100F, 30F};


    @Override
    public Div execute(Map<RichItem, Integer> tableOfContentsValues) {

        Div template = new Div();

        Paragraph heading = getHeadingParagraph();

        Table tableOfContents = generateTableOfContents(tableOfContentsValues);

        template.add(new AreaBreak());
        template.add(heading);
        template.add(tableOfContents);

        return template;
    }

    private Table generateTableOfContents(Map<RichItem, Integer> tableOfContents) {
        Table table = new Table(columnWidths);
        tableOfContents.forEach((item, integer) -> {
            table.addCell(item.getName());
            table.addCell("s." + integer);
        });
        table.useAllAvailableWidth();
        return table;
    }

    private Paragraph getHeadingParagraph() {
        Paragraph heading = new Paragraph("Spis treści:");
        heading.setTextAlignment(TextAlignment.CENTER);
        heading.setFontSize(20);
        return heading;
    }
}
