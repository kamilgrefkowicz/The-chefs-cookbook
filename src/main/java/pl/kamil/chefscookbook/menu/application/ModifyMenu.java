package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
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
public class ModifyMenu implements pl.kamil.chefscookbook.menu.application.port.ModifyMenuService {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final UserSecurityService userSecurity;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public Response<PoorMenu> createNewMenu(CreateNewMenuCommand command, Principal user) {

        if (menuRepository.findByNameAndUserEntityId(command.getMenuName(), Long.valueOf(user.getName())).isPresent())
            return Response.failure(MENU_NAME_TAKEN);

        Menu menu = newMenuCommandToMenu(command, user);
        return Response.success(new PoorMenu(menuRepository.save(menu)));
    }

    private Menu newMenuCommandToMenu(CreateNewMenuCommand command, Principal user) {
        return  new Menu(command.getMenuName(), userRepository.getOne(Long.valueOf(user.getName())));

    }

    @Override
    @Transactional
    public Response<MenuDto> addItemsToMenu(AddItemsToMenuCommand command, Principal user) {

        Menu menu = menuRepository.getOne(command.getMenuId());

        Set<Item> itemsToAdd = new HashSet<>();

        for (Long id : command.getItemIds()) {
            Optional<Item> opt = itemRepository.findById(id);
            if (opt.isEmpty()) return Response.failure(NOT_FOUND);
            Item item = opt.get();
            if (!userSecurity.belongsTo(item, user)) return Response.failure(NOT_AUTHORIZED);
            itemsToAdd.add(item);
        }
        menu.addItemsToMenu(itemsToAdd);
        return Response.success(new RichMenu(menuRepository.save(menu)));

    }

    @Override
    @Transactional
    public Response<MenuDto> removeItemFromMenu(RemoveItemFromMenuCommand command, Principal user) {

        Menu menu = menuRepository.getOne(command.getMenuId());

        Item toRemove = itemRepository.getOne(command.getItemId());

        menu.removeItem(toRemove);

        return Response.success(new RichMenu(menuRepository.save(menu)));

    }

    @Override
    public Response<Void> deleteMenu(DeleteMenuCommand command, Principal user) {

        Menu menu = menuRepository.getOne(command.getMenuId());

        if (!userSecurity.belongsTo(menu, user)) return Response.failure("You're not authorized to modify this menu");

        menuRepository.delete(menu);
        return Response.success(null);
    }
}
