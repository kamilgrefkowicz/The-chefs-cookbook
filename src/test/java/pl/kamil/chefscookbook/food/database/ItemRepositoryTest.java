package pl.kamil.chefscookbook.food.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.INTERMEDIATE;
import static pl.kamil.chefscookbook.food.domain.staticData.Unit.KILOGRAM;
import static pl.kamil.chefscookbook.user.domain.MasterUserConfig.getMasterUser;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    UserRepository userRepository;

    UserEntity masterUser;

    @BeforeEach
    void injectMasterUser() {
        masterUser = userRepository.save(getMasterUser());
    }

    @Test
    void findAllByUserEntityIdShouldReturnEmptyListIfNoItemsByUserPresent() {
        List<Item> queried = itemRepository.findAllByUserEntityId(1L);

        assertThat(queried, empty());
    }

    @Test
    void findAllByUserEntityIdShouldReturnOnlyListOfItemsOwnedByUser() {
        UserEntity user = saveUserEntity();
        Long userId = user.getId();
        UserEntity other = saveUserEntity();
        Item owned1 = saveItem(user);
        Item owned2 = saveItem(user);
        Item notOwned = saveItem(other);

        List<Item> queried = itemRepository.findAllByUserEntityId(userId);

        assertThat(queried, hasSize(2));
        assertThat(queried, contains(owned1, owned2));
    }

    @Test
    void findFirstByNameAndUserEntityIdShouldReturnEmptyIfNoItemWithNamePresent() {

        Optional<Item> queried = itemRepository.findFirstByNameAndUserEntityId("test", 3L);

        assertTrue(queried.isEmpty());
    }

    @Test
    void findFirstByNameAndUserEntityIdShouldReturnEmptyIfNoItemWithNamePresentByUser() {
        UserEntity other = saveUserEntity();
        Long otherId = other.getId();
        Item byOther = saveItem(other, "test");

        Optional<Item> queried = itemRepository.findFirstByNameAndUserEntityId("test", otherId + 1);

        assertTrue(queried.isEmpty());
    }

    @Test
    void findFirstByNameAndUserEntityIdShouldReturnPresentIfPresent() {
        UserEntity user = saveUserEntity();
        Item owned = saveItem(user, "test");

        Optional<Item> queried = itemRepository.findFirstByNameAndUserEntityId("test", user.getId());

        assertTrue(queried.isPresent());
        assertThat(queried.get(), equalTo(owned));
    }

    @Test
    void findForAutocompleteShouldReturnAllItemsByMasterUserWithMatchingName() {
        saveItem(masterUser, "test first", BASIC);
        saveItem(masterUser, "test second", BASIC);

        List<Item> queried = itemRepository.findForAutocomplete("test", 1L);

        assertThat(queried, hasSize(2));
    }
    @Test
    void findForAutocompleteShouldReturnAllBasicAndIntermediateItemsWithMatchingNameByUser() {
        UserEntity user = saveUserEntity();
        saveItem(user, "test first", BASIC);
        saveItem(user, "test second", INTERMEDIATE);

        List<Item> queried = itemRepository.findForAutocomplete("test", user.getId());

        assertThat(queried, hasSize(2));
    }

    private Item saveItem(UserEntity user, String name, Type type) {
        Item item = new Item(name, KILOGRAM, type, user);
        return itemRepository.save(item);
    }


    private Item saveItem(UserEntity user, String name) {
        Item item = saveItem(user);
        item.setName(name);
        return itemRepository.save(item);
    }

    private Item saveItem(UserEntity user) {
        Item item = new Item();
        item.setUserEntity(user);
        return itemRepository.save(item);
    }

    private UserEntity saveUserEntity() {
        UserEntity userEntity = new UserEntity();
        return userRepository.save(userEntity);
    }
}