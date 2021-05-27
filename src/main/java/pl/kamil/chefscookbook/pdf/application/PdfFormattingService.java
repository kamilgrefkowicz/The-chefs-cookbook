package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;

import java.io.*;

@Component
public class PdfFormattingService {

    public ByteArrayOutputStream generatePdfForItem(RichItem item) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(output);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);



        generateItemNameRow(item, document);
        generateYieldRow(item, document);
        generateIngredientList(item, document);

        generateDescriptionArea(item, document);

        document.close();

        return output;
    }

    private void generateDescriptionArea(RichItem item, Document document) {
        document.add(new Paragraph(item.getDescription()));
    }

    private void generateIngredientList(RichItem item, Document document) {
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

    private String getIngredientRow(pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto ingredient) {
        return ingredient.getAmount() + " " + ingredient.getChildItem().getUnit() + " " + ingredient.getChildItem().getName();
    }
}
