package pl.kamil.chefscookbook.pdf.application.port;

import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;

import java.io.ByteArrayOutputStream;

public interface PdfCreationService {

    ByteArrayOutputStream generatePdfForItem(RichItem item);

    ByteArrayOutputStream generatePdfForMenu(FullMenu menu);
}
