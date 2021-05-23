package pl.kamil.chefscookbook.user.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MasterUserConfig {

    @Bean
    UserEntity masterUser() {
        return new UserEntity("CCB", "");
    }
}
