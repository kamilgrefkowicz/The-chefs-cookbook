package pl.kamil.chefscookbook.user.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.security.Principal;

@Component
@AllArgsConstructor
public class UserSecurityService implements UserSecurityUseCase {

    private final UserEntity masterUser;

    @Override
    public boolean isOwner(Long userId, Principal user) {
        return userId.equals(Long.valueOf(user.getName()));
    }

    @Override
    public Response<Void> validateEligibilityForAddIngredient(Item parentItem, Item childItem, Principal user) {
        Long userId = Long.valueOf(user.getName());
        if (!parentItem.getUserEntity().getId().equals(userId))
            return Response.failure("You're not authorized to modify this item");
        if ((!childItem.getUserEntity().getId().equals(userId)) &&
                !childItem.getUserEntity().equals(masterUser))
            return Response.failure("You do not own this ingredient");

        return Response.success(null);
    }

}
