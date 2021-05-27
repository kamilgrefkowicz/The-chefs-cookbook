package pl.kamil.chefscookbook.menu.application.port;

import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;

import java.security.Principal;
import java.util.List;

public interface QueryMenuUseCase {

    List<PoorMenu> getAllMenusBelongingToUser(Principal user);
}
