package pl.kamil.chefscookbook.pdf.application.port;

import com.itextpdf.layout.Document;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;

import java.util.Set;

public interface GenerateListOfBasicIngredientsUseCase {

    void execute(Document document, Set<PoorItem> basics);
}
