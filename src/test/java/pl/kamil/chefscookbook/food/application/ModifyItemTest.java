package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.database.IngredientRepository;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.security.Principal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.ITEM_NAME_TAKEN;

@ExtendWith(MockitoExtension.class)
class ModifyItemTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserSecurityService userSecurity;

    @InjectMocks
    ModifyItem modifyItem;

    @Captor
    ArgumentCaptor<Item> itemCaptor;

    @Test
    void creatingItemShouldCallRepositoryToCheckIfNamePresent() {
        Principal user = getPrincipal(1L);
        Item alreadyExists = getItem("test item");
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.of(alreadyExists));

        modifyItem.createItem(new CreateNewItemCommand("test", Type.BASIC, Unit.KILOGRAM), user);

        verify(itemRepository).findFirstByNameAndUserEntityId("test", 1L);
    }

    @Test
    void creatingItemWhenSameItemNameByUserExistsShouldReturnFailure() {
        Principal user = getPrincipal(1L);
        Item alreadyExists = getItem("test item");
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.of(alreadyExists));

        Response<ItemDto> created = modifyItem.createItem(new CreateNewItemCommand("test", Type.BASIC, Unit.KILOGRAM), user);

        assertFalse(created.isSuccess());
        assertThat(created.getError(), equalTo(ITEM_NAME_TAKEN));
    }

    @Test
    void creatingValidItemShouldCallRepositoryToSave() {
        Principal user = getPrincipal(5L);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(5L);
        Item returned = getItem("returned test");
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(userEntity);
        when(itemRepository.save(any())).thenReturn(returned);

        modifyItem.createItem(new CreateNewItemCommand("test", Type.BASIC, Unit.KILOGRAM), user);

        verify(itemRepository).save(itemCaptor.capture());
        Item saved = itemCaptor.getValue();
        assertThat(saved.getName(), equalTo("test"));
        assertThat(saved.getType(), equalTo(Type.BASIC));
        assertThat(saved.getUnit(), equalTo(Unit.KILOGRAM));
        assertThat(saved.getUserEntity().getId(), equalTo(5L));
    }

    @Test
    void creatingValidItemShouldReturnSuccessfulResponse() {
        Principal principal = getPrincipal(5L);
        UserEntity userEntity = getUserEntity(5L);
        Item returned = getItem("returned test");
        returned.setUserEntity(userEntity);
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(userEntity);
        when(itemRepository.save(any())).thenReturn(returned);

        Response<ItemDto> created = modifyItem.createItem(new CreateNewItemCommand("test", Type.BASIC, Unit.KILOGRAM), principal);

        assertTrue(created.isSuccess());
        ItemDto dto = created.getData();
        assertThat(dto.getName(), equalTo("returned test"));
        assertThat(dto.getType(), equalTo(Type.BASIC));
        assertThat(dto.getUnit(), equalTo(Unit.KILOGRAM));
        assertThat(dto.getUserEntityId(), equalTo(5L));
    }

    private UserEntity getUserEntity(long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        return user;
    }

    private Principal getPrincipal(long id) {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(id);
            }
        };
    }

    private Item getItem(String name) {
        return new Item(name, Unit.KILOGRAM, Type.BASIC, new UserEntity());

    }

}