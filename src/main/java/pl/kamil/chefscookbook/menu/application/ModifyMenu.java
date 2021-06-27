package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.exceptions.NotAuthorizedException;
import pl.kamil.chefscookbook.shared.exceptions.NotFoundException;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.*;

@Service
@AllArgsConstructor
public class ModifyMenu implements ModifyMenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final UserSecurityService userSecurity;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Response<RichMenu> createNewMenu(CreateNewMenuCommand command, Principal user) {

        if (menuRepository.findByNameAndUserEntityId(command.getMenuName(), Long.valueOf(user.getName())).isPresent())
            return Response.failure(MENU_NAME_TAKEN);

        Menu menu = newMenuCommandToMenu(command, user);
        return successfulResponse(menu, MENU_CREATED);
    }

    private Response<RichMenu> successfulResponse(Menu menu, String message) {
        return Response.success(new RichMenu(menuRepository.save(menu)), message);
    }

    private Menu newMenuCommandToMenu(CreateNewMenuCommand command, Principal user) {
        return  new Menu(command.getMenuName(), userRepository.getOne(Long.valueOf(user.getName())));

    }

    @SneakyThrows
    @Override
    @Transactional
    public Response<RichMenu> addItemsToMenu(AddItemsToMenuCommand command, Principal user) {

        Menu menu = authorize(command.getMenuId(), user);

        Set<Item> itemsToAdd = new HashSet<>();

        for (Long id : command.getItemIds()) {
            Optional<Item> opt = itemRepository.findById(id);
            if (opt.isEmpty()) throw new NotFoundException();
            Item item = opt.get();
            if (!userSecurity.belongsTo(item, user)) throw new NotAuthorizedException();
            itemsToAdd.add(item);
        }
        menu.addItemsToMenu(itemsToAdd);
        return successfulResponse(menu, ITEMS_ADDED);

    }

    @Override
    @Transactional
    public Response<RichMenu> removeItemFromMenu(RemoveItemFromMenuCommand command, Principal user) {

        Menu menu = authorize(command.getMenuId(), user);

        Item toRemove = itemRepository.getOne(command.getItemId());

        menu.removeItem(toRemove);

        return successfulResponse(menu, ITEMS_REMOVED);

    }

    @Override
    public Response<Void> deleteMenu(DeleteMenuCommand command, Principal user) {

        Menu menu = authorize(command.getMenuId(), user);

        menuRepository.delete(menu);

        return Response.success(null, MENU_DELETED);
    }

    @SneakyThrows
    private Menu authorize(Long menuId, Principal user) {
        Optional<Menu> queried = menuRepository.findById(menuId);
        if (queried.isEmpty()) throw new NotFoundException();
        Menu menu = queried.get();
        if (!userSecurity.belongsTo(menu, user)) throw new NotAuthorizedException();

        return menu;
    }
}
