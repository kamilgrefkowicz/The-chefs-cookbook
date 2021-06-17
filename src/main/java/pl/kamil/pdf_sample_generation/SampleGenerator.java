package pl.kamil.pdf_sample_generation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.kamil.chefscookbook.pdf.application", "pl.kamil.pdf_sample_generation"})
public class SampleGenerator {

    // This entire package generates a single pdf file used for testing purposes.
    // Whenever the PdfCreationTest tests fail, run this class to generate new pdfs, inspect to see if happy with result and then run tests again.
    // If running locally, make sure to update file destination path in FileDestinationHolder class.

    public static void main(String[] args) {
        SpringApplication.run(SampleGenerator.class, args);
    }

}
