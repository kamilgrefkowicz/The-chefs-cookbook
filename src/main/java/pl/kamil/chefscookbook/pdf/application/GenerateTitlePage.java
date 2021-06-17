package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.pdf.application.port.GenerateTitlePageUseCase;

@Component
public class GenerateTitlePage implements GenerateTitlePageUseCase {
    @Override
    public void execute(Document document, FullMenu menu) {

        Paragraph menuName = getMenuNameParagraph(menu);

        Paragraph generatedBy = getGeneratedByParagraph();

        document.add(menuName);
        document.add(emptyLine());
        document.add(generatedBy);

        document.add(new AreaBreak());

    }

    private Paragraph emptyLine() {
        return new Paragraph("");
    }

    private Paragraph getGeneratedByParagraph() {
        Paragraph generatedBy = new Paragraph(getGeneratedByText());
        generatedBy.setFontSize(20);
        generatedBy.setTextAlignment(TextAlignment.CENTER);
        return generatedBy;
    }

    private Paragraph getMenuNameParagraph(FullMenu menu) {
        Paragraph menuName = new Paragraph(menu.getMenuName());
        menuName.setFontSize(25);
        menuName.setBold();
        menuName.setTextAlignment(TextAlignment.CENTER);
        return menuName;
    }

    private String getGeneratedByText() {
        return "Wygenerowane przez Chefs CookBook";
    }
}
