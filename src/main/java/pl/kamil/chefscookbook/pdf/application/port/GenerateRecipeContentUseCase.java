package pl.kamil.chefscookbook.pdf.application.port;

import com.itextpdf.layout.element.Div;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;

public interface GenerateRecipeContentUseCase {


    Div execute(RichItem item);
}
