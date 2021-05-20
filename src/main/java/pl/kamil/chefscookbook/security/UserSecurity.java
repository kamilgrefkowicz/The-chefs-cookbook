package pl.kamil.chefscookbook.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    public boolean isOwner(String objectOwner, User user) {
        return user.getUsername().equalsIgnoreCase(objectOwner);
    }
}
