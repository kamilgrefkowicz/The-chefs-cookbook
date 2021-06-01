package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.menu.application.dto.RichMenu.convertToRichMenu;


@Service
@AllArgsConstructor
public class QueryMenu implements QueryMenuUseCase {

    private final MenuRepository menuRepository;
    private final UserSecurityUseCase userSecurity;

    @Override
    public List<RichMenu> getAllMenusBelongingToUser(Principal user) {
        return menuRepository.findAllByUserEntityId(Long.valueOf(user.getName()))
                .stream()
                .map(RichMenu::convertToRichMenu)
                .collect(Collectors.toList());
    }

    @Override
    public Response<RichMenu> findById(Long menuId, Principal user) {
        Menu menu = menuRepository.getOne(menuId);

        if (!userSecurity.isOwner(menu.getUserEntity().getId(), user)) return Response.failure("You do not own this menu");

        return Response.success(convertToRichMenu(menu));

    }
}
