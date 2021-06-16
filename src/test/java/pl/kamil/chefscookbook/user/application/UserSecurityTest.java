package pl.kamil.chefscookbook.user.application;

import org.junit.jupiter.api.Test;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

class UserSecurityTest {

    UserSecurity userSecurity = new UserSecurity();

    @Test
    void belongsToShouldReturnTrueIfInFactObjectBelongsToPrincipal() {
        Item item = new Item();
        Principal principal = getPrincipal(1L);
        UserEntity userEntity = getUserEntity(1L);
        item.setUserEntity(userEntity);

        boolean result = userSecurity.belongsTo(item, principal);

        assertTrue(result);
    }
    @Test
    void belongsToShouldReturnFalseIfObjectDoesNotBelongToPrincipal() {
        Item item = new Item();
        Principal principal = getPrincipal(1L);
        UserEntity userEntity = getUserEntity(2L);
        item.setUserEntity(userEntity);

        boolean result = userSecurity.belongsTo(item, principal);

        assertFalse(result);
    }
    @Test
    void belongsToOrIsPublicShouldReturnTrueIfBelongsToPrincipal() {
        Item item = new Item();
        Principal principal = getPrincipal(1L);
        UserEntity userEntity = getUserEntity(1L);
        item.setUserEntity(userEntity);

        boolean result = userSecurity.belongsToOrIsPublic(item, principal);

        assertTrue(result);
    }
    @Test
    void belongsToOrIsPublicShouldReturnTrueIfBelongsToMasterUser() {
        Item item = new Item();
        Principal principal = getPrincipal(1L);
        UserEntity userEntity = getMasterUser();
        item.setUserEntity(userEntity);

        boolean result = userSecurity.belongsToOrIsPublic(item, principal);

        assertTrue(result);
    }
    @Test
    void belongsToOrIsPublicShouldReturnFalseIfNeitherConditionMet() {
        Item item = new Item();
        Principal principal = getPrincipal(2L);
        UserEntity userEntity = getUserEntity(1L);
        item.setUserEntity(userEntity);

        boolean result = userSecurity.belongsToOrIsPublic(item, principal);

        assertFalse(result);
    }

    private UserEntity getUserEntity(Long id) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        return userEntity;
    }

    private Principal getPrincipal(Long id) {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(id);
            }
        };
    }
}