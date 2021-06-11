package pl.kamil.chefscookbook.user.application.port;

import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.OwnedEntity;

import java.security.Principal;

public interface UserSecurityService {

    boolean belongsTo(OwnedEntity object, Principal user);

    boolean belongsToOrIsPublic(Item childItem, Principal user);
}
