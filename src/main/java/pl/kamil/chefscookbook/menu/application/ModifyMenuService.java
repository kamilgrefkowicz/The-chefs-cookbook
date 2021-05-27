package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.database.UserRepository;

import java.security.Principal;

import static pl.kamil.chefscookbook.menu.application.dto.MenuDto.convertToPoorMenu;

@Service
@AllArgsConstructor
public class ModifyMenuService implements ModifyMenuUseCase {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    @Override
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
    public Response<PoorMenu> addItemsToMenu(AddItemsToMenuCommand command, Principal user) {
        return null;
    }
}
