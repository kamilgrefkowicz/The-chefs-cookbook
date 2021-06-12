package pl.kamil.chefscookbook.user.application;

import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.OwnedEntity;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;

import java.security.Principal;

import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@Component
public class UserSecurity implements UserSecurityService {


    @Override
    public boolean belongsTo(OwnedEntity object, Principal user) {
        return object.getUserEntity().getId().equals(Long.valueOf(user.getName()));
    }

    @Override
    public boolean belongsToOrIsPublic(Item childItem, Principal user) {

        return childItem.getUserEntity().getId().equals(Long.valueOf(user.getName())) || childItem.getUserEntity().equals(getMasterUser());
    }


}
