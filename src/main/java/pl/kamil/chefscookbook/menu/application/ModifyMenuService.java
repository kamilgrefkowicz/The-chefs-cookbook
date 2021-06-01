package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Arrays;
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
        Menu menu = newMenuCommandToMenu(command, user);

        //todo:check if name taken

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
    public Response<RichMenu> addItemsToMenu(AddItemsToMenuCommand command, Principal user) {

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


}
