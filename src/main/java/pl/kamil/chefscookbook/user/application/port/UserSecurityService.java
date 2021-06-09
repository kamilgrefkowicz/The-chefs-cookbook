package pl.kamil.chefscookbook.user.application.port;

import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.Null;
import java.security.Principal;

public interface UserSecurityService {

    boolean isOwner(Long userId, Principal user);

    boolean isEligibleForAddIngredient(Item parentItem, Item childItem, Principal user);

}
