package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static pl.kamil.chefscookbook.menu.application.dto.PoorMenu.convertToPoorMenu;
import static pl.kamil.chefscookbook.menu.application.dto.RichMenu.convertToRichMenu;

@Service
@AllArgsConstructor
public class ModifyMenuService implements ModifyMenuUseCase {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final UserSecurityUseCase userSecurity;
    private final ItemJpaRepository itemRepository;

    @Override
    @Transactional
    public Response<PoorMenu> createNewMenu(CreateNewMenuCommand command, Principal user) {

        if (menuRepository.findByNameAndUserEntityId(command.getMenuName(), Long.valueOf(user.getName())).isPresent())
            return Response.failure("You already have a menu called: " + command.getMenuName());

        Menu menu = newMenuCommandToMenu(command, user);
        return Response.success(convertToPoorMenu(menuRepository.save(menu)));
    }

    private Menu newMenuCommandToMenu(CreateNewMenuCommand command, Principal user) {
        return  Menu.builder()
                .name(command.getMenuName())
                .userEntity(userRepository.getOne(Long.valueOf(user.getName())))
                .build();
    }

    @Override
    @Transactional
    public Response<MenuDto> addItemsToMenu(AddItemsToMenuCommand command, Principal user) {

        Menu menu = menuRepository.getOne(command.getMenuId());

        Set<Item> itemsToAdd = new HashSet<>();

        for (Long id : command.getItemIds()) {
            Optional<Item> opt = itemRepository.findById(id);
            if (opt.isEmpty()) return Response.failure("Item with id: " + id + " does not exist");
            Item item = opt.get();
            if (!userSecurity.isOwner(item.getUserEntity().getId(), user)) return Response.failure("You're not authorized to use this item");
            itemsToAdd.add(item);
        }
        menu.addItemsToMenu(itemsToAdd);
        return Response.success(convertToRichMenu(menuRepository.save(menu)));

    }

    @Override
    @Transactional
    public Response<MenuDto> removeItemFromMenu(RemoveItemFromMenuCommand command, Principal user) {

        Menu menu = menuRepository.getOne(command.getMenuId());

        Item toRemove = itemRepository.getOne(command.getItemId());

        menu.removeItem(toRemove);

        return Response.success(convertToRichMenu(menuRepository.save(menu)));

    }

    @Override
    public Response<Void> deleteMenu(DeleteMenuCommand command, Principal user) {

        Menu menu = menuRepository.getOne(command.getMenuId());

        if (!userSecurity.isOwner(menu.getUserEntity().getId(), user)) return Response.failure("You're not authorized to modify this menu");

        menuRepository.delete(menu);
        return Response.success(null);
    }
}
