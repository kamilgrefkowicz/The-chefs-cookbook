package pl.kamil.chefscookbook.user.application;

import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.security.Principal;

import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@Component
public class UserSecurity implements pl.kamil.chefscookbook.user.application.port.UserSecurityService {


    @Override
    public boolean isOwner(Long userId, Principal user) {
        return userId.equals(Long.valueOf(user.getName()));
    }

    @Override
    public boolean isEligibleForAddIngredient(Item parentItem, Item childItem, Principal user) {
        Long userId = Long.valueOf(user.getName());
        if (!parentItem.getUserEntity().getId().equals(userId))
            return false;
        if ((!childItem.getUserEntity().getId().equals(userId)) &&
                !childItem.getUserEntity().equals(getMasterUser()))
            return false;

        return true;
    }

}
