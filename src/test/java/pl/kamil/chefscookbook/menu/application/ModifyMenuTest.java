package pl.kamil.chefscookbook.menu.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.AddItemsToMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.CreateNewMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.RemoveItemFromMenuCommand;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.security.Principal;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.DeleteMenuCommand;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.MENU_NAME_TAKEN;

@ExtendWith(MockitoExtension.class)
class ModifyMenuTest {
    @Mock
    MenuRepository menuRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserSecurityService userSecurity;
    @Mock
    ItemRepository itemRepository;

    @Captor
    ArgumentCaptor<Menu> menuCaptor;

    @InjectMocks
    ModifyMenu modifyMenu;


    @Test
    void creatingNewMenuShouldCallRepositoryToCheckIfMenuNameTaken() {
        CreateNewMenuCommand passed = new CreateNewMenuCommand("test");
        Principal user = getPrincipalWithIdOf1();
        when(menuRepository.findByNameAndUserEntityId(any(), any())).thenReturn(Optional.of(getMenu()));

        modifyMenu.createNewMenu(passed, user);

        verify(menuRepository).findByNameAndUserEntityId("test", 1L);
    }
    @Test
    void creatingMenuWhenNameTakenShouldReturnFailure() {
        CreateNewMenuCommand passed = new CreateNewMenuCommand("test");
        Principal user = getPrincipalWithIdOf1();
        when(menuRepository.findByNameAndUserEntityId(any(), any())).thenReturn(Optional.of(getMenu()));

        Response<RichMenu> response = modifyMenu.createNewMenu(passed, user);

        assertFalse(response.isSuccess());
        assertThat(response.getError(), equalTo(MENU_NAME_TAKEN));
        verify(menuRepository, times(0)).save(any());
    }
    @Test
    void creatingValidMenuShouldRetrieveUserEntityFromRepository() {
        CreateNewMenuCommand passed = new CreateNewMenuCommand("test");
        Principal user = getPrincipalWithIdOf1();
        when(menuRepository.findByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(getUserEntity());
        when(menuRepository.save(any())).thenReturn(getMenu());

        modifyMenu.createNewMenu(passed, user);

        verify(userRepository).getOne(1L);
    }
    @Test
    void creatingValidMenuShouldCallRepositoryToSave() {
        CreateNewMenuCommand passed = new CreateNewMenuCommand("test");
        Principal principal = getPrincipalWithIdOf1();
        UserEntity userEntity = getUserEntity();
        when(menuRepository.findByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(userEntity);
        when(menuRepository.save(any())).thenReturn(getMenu());

        modifyMenu.createNewMenu(passed, principal);

        verify(menuRepository).save(menuCaptor.capture());
        Menu saved = menuCaptor.getValue();
        assertThat(saved.getName(), equalTo("test"));
        assertThat(saved.getUserEntity(), equalTo(userEntity));
    }
    @Test
    void successfulMenuModificationShouldReturnCorrectResponse() {
        CreateNewMenuCommand passed = new CreateNewMenuCommand("test");
        Principal principal = getPrincipalWithIdOf1();
        UserEntity userEntity = getUserEntity();
        when(menuRepository.findByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(userEntity);
        Menu returned = getMenu();

        when(menuRepository.save(any())).thenReturn(returned);

        Response<RichMenu> response = modifyMenu.createNewMenu(passed, principal);
        RichMenu saved = response.getData();

        assertTrue(response.isSuccess());
        assertThat(saved.getMenuName(), equalTo("test"));
        assertThat(saved.getMenuId(), equalTo(1L));
    }
    @Test
    void addingItemsToMenuShouldQueryRepositoryForAllItems() {
        Principal principal = getPrincipalWithIdOf1();
        passSecurity();
        when(itemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        when(menuRepository.save(any())).thenReturn(getMenu());
        AddItemsToMenuCommand command = new AddItemsToMenuCommand(1L, new Long[]{2L, 3L});

        modifyMenu.addItemsToMenu(command, principal);

        verify(itemRepository, times(1)).findById(2L);
        verify(itemRepository, times(1)).findById(3L);
    }
    @Test
    void successfulAddItemToMenuShouldCallRepositoryToSaveUpdatedMenu() {
        Principal principal = getPrincipalWithIdOf1();
        passSecurity();
        Item returned1 = getItem(1L);
        Item returned2 = getItem(2L);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(returned1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(returned2));
        when(menuRepository.save(any())).thenReturn(getMenu());
        AddItemsToMenuCommand command = new AddItemsToMenuCommand(1L, new Long[]{1L, 2L});

        modifyMenu.addItemsToMenu(command, principal);

        verify(menuRepository).save(menuCaptor.capture());
        Menu saved = menuCaptor.getValue();
        assertThat(saved.getItems(), hasSize(2));
    }
    @Test
    void removeItemFromMenuShouldCallRepositoryToRetrieveItem() {
        Principal principal = getPrincipalWithIdOf1();
        passSecurity();
        when(menuRepository.save(any())).thenReturn(getMenu());
        RemoveItemFromMenuCommand command = new RemoveItemFromMenuCommand(1L, 2L);

        modifyMenu.removeItemFromMenu(command, principal);

        verify(itemRepository).getOne(2L);
    }
    @Test
    void removeItemFromMenuShouldCallRepositoryToSaveUpdatedMenu() {
        Principal principal = getPrincipalWithIdOf1();
        Item toRemove = getItem(2L);
        Menu menu = getMenu();
        menu.addSingleItemToMenu(toRemove);
        when(menuRepository.findById(any())).thenReturn(Optional.of(menu));
        when(userSecurity.belongsTo(any(), any())).thenReturn(true);
        when(itemRepository.getOne(2L)).thenReturn(toRemove);
        when(menuRepository.save(any())).thenReturn(getMenu());
        RemoveItemFromMenuCommand command = new RemoveItemFromMenuCommand(1L, 2L);

        modifyMenu.removeItemFromMenu(command, principal);

        verify(menuRepository).save(menuCaptor.capture());
        Menu saved = menuCaptor.getValue();
        assertThat(saved.getItems(), hasSize(0));
    }
    @Test
    void deletingMenuShouldCallRepositoryToDelete() {
        Principal principal = getPrincipalWithIdOf1();
        passSecurity();
        DeleteMenuCommand command = new DeleteMenuCommand(3L);

        modifyMenu.deleteMenu(command, principal);

        verify(menuRepository).delete(any());
    }

    private void passSecurity() {
        when(menuRepository.findById(any())).thenReturn(Optional.of(getMenu()));
        when(userSecurity.belongsTo(any(), any())).thenReturn(true);
    }


    private Item getItem(Long itemId) {
        Item item = new Item();
        item.setId(itemId);
        return item;
    }


    private UserEntity getUserEntity() {
        return new UserEntity("", "");
    }



    private Principal getPrincipalWithIdOf1() {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(1L);
            }
        };
    }

    private Menu getMenu() {
        Menu menu = new Menu();
        menu.setName("test");
        menu.setId(1L);
        return menu;
    }


}












