package pl.kamil.chefscookbook.pdf.application.port;

import com.itextpdf.layout.Document;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;

public interface GenerateRecipeContentUseCase {


    int execute(Document document, RichItem item);
}
