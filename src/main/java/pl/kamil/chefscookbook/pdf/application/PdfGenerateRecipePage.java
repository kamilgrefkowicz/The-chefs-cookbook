package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.pdf.application.port.GenerateRecipePageUseCase;

@Component
public class PdfGenerateRecipePage implements GenerateRecipePageUseCase {
    @Override
    public void execute(Document document, RichItem item) {
        generateItemNameRow(item, document);
        generateYieldRow(item, document);
        generateIngredientList(item, document);

        generateDescriptionArea(item, document);
    }


    private void generateDescriptionArea(RichItem item, Document document) {
        document.add(new Paragraph(item.getDescription()));
    }

    private void generateIngredientList(RichItem item, Document document) {
        //this is an Itext list, not a Java one
        List ingredients = new List();
        item.getIngredients().forEach(ingredient ->
                ingredients.add(getIngredientRow(ingredient)));
        document.add(ingredients);
    }

    private void generateYieldRow(RichItem item, Document document) {
        document.add(new Paragraph("Przepis na " + item.getRecipeYield() + " " + item.getUnit()));
    }

    private void generateItemNameRow(RichItem item, Document document) {
        document.add(new Paragraph(item.getName()));
    }

    private String getIngredientRow(IngredientDto ingredient) {
        return ingredient.getAmount() + " " + ingredient.getChildItem().getUnit() + " " + ingredient.getChildItem().getName();
    }
}
