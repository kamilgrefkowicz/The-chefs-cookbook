package pl.kamil.pdf_sample_generation;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.pdf.application.port.PdfCreationService;

import java.io.*;

import static pl.kamil.pdf_sample_generation.FileDestinationHolder.PDF_SAMPLE_FOLDER;

@Component
@RequiredArgsConstructor
public class SampleGeneratorRunner implements CommandLineRunner {

    private final PdfCreationService pdfCreation;
    private final FullMenu fullMenu;

    @Override
    public void run(String... args) throws Exception {

        generatePdfForMenuSample(fullMenu);
    }

    private void generatePdfForMenuSample(FullMenu fullMenu) throws IOException {

        ByteArrayOutputStream output = pdfCreation.generatePdfForMenu(fullMenu);
        File file = new File(PDF_SAMPLE_FOLDER  + "sample menu.pdf");

        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(output.toByteArray());
        }
    }

}
