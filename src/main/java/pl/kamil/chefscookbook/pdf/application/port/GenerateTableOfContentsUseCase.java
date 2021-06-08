package pl.kamil.chefscookbook.pdf.application.port;

import com.itextpdf.layout.Document;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;

import java.util.Map;

public interface GenerateTableOfContentsUseCase {

    void execute (Document document, Map<RichItem, Integer> tableOfContentsValues);
}
