package pl.kamil.chefscookbook.menu.application.port;

import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.shared.response.Response;

import java.security.Principal;
import java.util.List;

public interface QueryMenuUseCase {

    List<RichMenu> getAllMenusBelongingToUser(Principal user);

    Response<RichMenu> findById(Long menuId, Principal user);

}
