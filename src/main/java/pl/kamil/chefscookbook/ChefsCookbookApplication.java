package pl.kamil.chefscookbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ChefsCookbookApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChefsCookbookApplication.class, args);
    }

}
