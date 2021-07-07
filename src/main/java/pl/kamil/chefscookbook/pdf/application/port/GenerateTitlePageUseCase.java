package pl.kamil.chefscookbook.pdf.application.port;


import com.itextpdf.layout.element.Div;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;

public interface GenerateTitlePageUseCase {

    Div execute (FullMenu menu);


}
