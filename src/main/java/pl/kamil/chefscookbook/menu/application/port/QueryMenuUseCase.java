package pl.kamil.chefscookbook.menu.application.port;

import pl.kamil.chefscookbook.menu.application.dto.RichMenu;

import java.security.Principal;
import java.util.List;

public interface QueryMenuUseCase {

    List<RichMenu> getAllMenusBelongingToUser(Principal user);
}
