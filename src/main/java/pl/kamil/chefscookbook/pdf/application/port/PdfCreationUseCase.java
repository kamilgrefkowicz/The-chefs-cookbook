package pl.kamil.chefscookbook.pdf.application.port;

import pl.kamil.chefscookbook.food.application.dto.item.RichItem;

import java.io.ByteArrayOutputStream;

public interface PdfCreationUseCase {

    ByteArrayOutputStream generatePdfForItem(RichItem item);
}
