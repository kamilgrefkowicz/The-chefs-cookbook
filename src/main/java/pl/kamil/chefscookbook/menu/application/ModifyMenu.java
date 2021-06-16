package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
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
        return successfulResponse(menu);
    }

    private Response<RichMenu> successfulResponse(Menu menu) {
        return Response.success(new RichMenu(menuRepository.save(menu)));
    }

    private Menu newMenuCommandToMenu(CreateNewMenuCommand command, Principal user) {
        return  new Menu(command.getMenuName(), userRepository.getOne(Long.valueOf(user.getName())));

    }

    @Override
    @Transactional
    public Response<RichMenu> addItemsToMenu(AddItemsToMenuCommand command, Principal user) {

        Response<Menu> securityPass = passSecurity(command.getMenuId(), user);
        if (!securityPass.isSuccess()) return Response.failure(securityPass.getError());
        Menu menu = securityPass.getData();

        Set<Item> itemsToAdd = new HashSet<>();

        for (Long id : command.getItemIds()) {
            Optional<Item> opt = itemRepository.findById(id);
            if (opt.isEmpty()) return Response.failure(NOT_FOUND);
            Item item = opt.get();
            if (!userSecurity.belongsTo(item, user)) return Response.failure(NOT_AUTHORIZED);
            itemsToAdd.add(item);
        }
        menu.addItemsToMenu(itemsToAdd);
        return successfulResponse(menu);

    }

    @Override
    @Transactional
    public Response<RichMenu> removeItemFromMenu(RemoveItemFromMenuCommand command, Principal user) {

        Response<Menu> securityPass = passSecurity(command.getMenuId(), user);
        if (!securityPass.isSuccess()) return Response.failure(securityPass.getError());
        Menu menu = securityPass.getData();

        Item toRemove = itemRepository.getOne(command.getItemId());

        menu.removeItem(toRemove);

        return successfulResponse(menu);

    }

    @Override
    public Response<Void> deleteMenu(DeleteMenuCommand command, Principal user) {

        Response<Menu> securityPass = passSecurity(command.getMenuId(), user);
        if (!securityPass.isSuccess()) return Response.failure(securityPass.getError());
        Menu menu = securityPass.getData();

        menuRepository.delete(menu);

        return Response.success(null);
    }

    private Response<Menu> passSecurity(Long menuId, Principal user) {
        Optional<Menu> queried = menuRepository.findById(menuId);
        if (queried.isEmpty()) return Response.failure(NOT_FOUND);
        Menu menu = queried.get();
        if (!userSecurity.belongsTo(menu, user)) return Response.failure(NOT_AUTHORIZED);

        return Response.success(menu);
    }
}
