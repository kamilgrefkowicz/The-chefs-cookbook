package pl.kamil.chefscookbook.pdf.application.port;

import com.itextpdf.layout.element.Div;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;

import java.util.Map;

public interface GenerateTableOfContentsUseCase {

    Div execute (Map<RichItem, Integer> tableOfContentsValues);
}
