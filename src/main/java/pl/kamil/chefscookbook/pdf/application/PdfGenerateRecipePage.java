package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.pdf.application.port.GenerateRecipePageUseCase;


@Component
public class PdfGenerateRecipePage implements GenerateRecipePageUseCase {
    private final Rectangle[] COLUMNS = {
            new Rectangle(36, 36, 500, 100),
            new Rectangle(36, 210, 254, 706),
            new Rectangle(305, 210, 254, 706)
    };
    private final float[] columnWidths = {254F, 254F};

    @Override
    public void execute(Document document, RichItem item) {


        document.add(generateItemNameRow(item));
        document.add(generateYieldRowForIntermediates(item));

        Table table = new Table(columnWidths);
        table.addCell(generateIngredientList(item));
        table.addCell(generateDescriptionArea(item));

        document.add(table);


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
        if (item.getType().equals(Type.BASIC())) return new Paragraph("");

        Paragraph paragraph = new Paragraph("Przepis na " + item.getRecipeYield() + " " + item.getUnit());
        paragraph.setTextAlignment(TextAlignment.CENTER);

        return paragraph;
    }

    private Paragraph generateItemNameRow(RichItem item) {
        Paragraph paragraph = new Paragraph(item.getName());
        paragraph.setFontSize(14);
        paragraph.setTextAlignment(TextAlignment.CENTER);
        return paragraph;
    }

    private String getIngredientRow(IngredientDto ingredient) {
        return ingredient.getAmount() + " " + ingredient.getChildItem().getUnit() + " " + ingredient.getChildItem().getName();
    }
}
