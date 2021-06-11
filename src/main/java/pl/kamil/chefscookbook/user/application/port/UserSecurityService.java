package pl.kamil.chefscookbook.user.application.port;

import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.OwnedEntity;

import java.security.Principal;

public interface UserSecurityService {

    boolean belongsTo(OwnedEntity object, Principal user);

    boolean isEligibleForAddIngredient(Item parentItem, Item childItem, Principal user);

}
