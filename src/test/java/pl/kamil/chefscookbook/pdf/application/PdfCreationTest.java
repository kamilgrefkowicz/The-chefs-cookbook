package pl.kamil.chefscookbook.pdf.application;

import com.itextpdf.kernel.utils.CompareTool;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.pdf_sample_generation.SampleMenuConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static pl.kamil.pdf_sample_generation.FileDestinationHolder.PDF_SAMPLE_FOLDER;
import static pl.kamil.pdf_sample_generation.FileDestinationHolder.PDF_TEST_FOLDER;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {PdfCreation.class,
        GenerateListOfBasicIngredients.class,
        GenerateRecipeContent.class,
        GenerateTableOfContents.class,
        GenerateTitlePage.class,
        SampleMenuConfiguration.class})
class PdfCreationTest {

    @Autowired
    PdfCreation pdfCreation;

    @Autowired
    FullMenu fullMenu;

    // Unfortunately, Itext 7 has very poor documentation when it comes to unit tests.
    // Therefore, instead of unit testing I decided to use this setup to quicken the process of visually inspecting generated pdfs.
    // To generate sample Pdfs, run SampleGenerator class.


    @SneakyThrows
    @Test
    void compareMenu() {
        String cmpPdf = PDF_TEST_FOLDER + "test menu.pdf";
        ByteArrayOutputStream output = pdfCreation.generatePdfForMenu(fullMenu);
        File file = new File(cmpPdf);

        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(output.toByteArray());
        }
        CompareTool compareTool = new CompareTool();

        String outPdf = PDF_SAMPLE_FOLDER + "sample menu.pdf";

        String result = compareTool.compareTagStructures(outPdf, cmpPdf);

        // this will be null if the pdf contents are identical
        assertNull(result);

    }
}