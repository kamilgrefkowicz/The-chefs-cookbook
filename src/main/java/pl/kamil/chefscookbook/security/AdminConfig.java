package pl.kamil.chefscookbook.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Data
@ConfigurationProperties("app.security.admin")
 class AdminConfig {
    private String username;
    private String password;

    User CCB() {
        return new User(username, password, Collections.emptySet());
    }
}
