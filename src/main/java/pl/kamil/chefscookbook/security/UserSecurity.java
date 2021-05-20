package pl.kamil.chefscookbook.security;

import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class UserSecurity {

    public boolean isOwner(Long objectOwnerId, Principal user) {
        return Long.valueOf(user.getName()).equals(objectOwnerId);
    }
}
