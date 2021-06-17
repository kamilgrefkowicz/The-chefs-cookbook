package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.pdf.application.port.GenerateRecipeContentUseCase;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;


@Component
public class GenerateRecipeContent implements GenerateRecipeContentUseCase {

    private final float[] columnWidths = {254F, 254F};

    @Override
    public int execute(Document document, RichItem item) {

        Div template = new Div();
        template.setKeepTogether(true);

        template.add(generateItemNameRow(item));
        int currentPage = document.getPdfDocument().getNumberOfPages();
        template.add(generateYieldRowForIntermediates(item));

        Paragraph paragraph = new Paragraph();

        Table table = new Table(columnWidths);
        paragraph.add(table);
        table.addCell(generateIngredientList(item));
        table.addCell(generateDescriptionArea(item));

        template.add(table);
        template.add(new Paragraph("").setFontSize(40));

        document.add(template);
        return currentPage;
    }


    private Paragraph generateDescriptionArea(RichItem item) {
        return new Paragraph(item.getDescription());
    }

    private List generateIngredientList(RichItem item) {
        //this is an Itext list, not a Java one
        List ingredients = new List();
        item.getIngredients().forEach(ingredient ->
                ingredients.add(getIngredientRow(ingredient)));
        return ingredients;
    }

    private Paragraph generateYieldRowForIntermediates(RichItem item) {
        if (item.getType().equals(DISH)) return new Paragraph("");

        Paragraph paragraph = new Paragraph("Przepis na " + item.getRecipeYield() + " " + item.getUnit());
        paragraph.setTextAlignment(TextAlignment.CENTER);

        return paragraph;
    }

    private Paragraph generateItemNameRow(RichItem item) {
        Paragraph paragraph = new Paragraph(item.getName());
        paragraph.setFontSize(16);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        return paragraph;
    }

    private String getIngredientRow(IngredientDto ingredient) {
        if (ingredient.getAmount().equals(new BigDecimal("0.00"))) {
            return "troszkę " + ingredient.getChildItem().getName();
        }
        return ingredient.getAmount() + " " + ingredient.getChildItem().getUnit() + " " + ingredient.getChildItem().getName();
    }
}
