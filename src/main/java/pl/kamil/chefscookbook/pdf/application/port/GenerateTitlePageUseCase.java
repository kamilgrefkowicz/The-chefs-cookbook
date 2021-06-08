package pl.kamil.chefscookbook.pdf.application.port;


import com.itextpdf.layout.Document;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;

public interface GenerateTitlePageUseCase {

    void execute (Document document, FullMenu menu);


}
