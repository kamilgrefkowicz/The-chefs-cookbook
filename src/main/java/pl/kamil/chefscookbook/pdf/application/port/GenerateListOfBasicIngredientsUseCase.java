package pl.kamil.chefscookbook.pdf.application.port;

import com.itextpdf.layout.element.Div;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;

import java.util.Set;

public interface GenerateListOfBasicIngredientsUseCase {

    Div execute(Set<PoorItem> basics);
}
