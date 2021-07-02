package pl.kamil.chefscookbook.menu.application.port;

import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.shared.exceptions.NotAuthorizedException;
import pl.kamil.chefscookbook.shared.exceptions.NotFoundException;
import pl.kamil.chefscookbook.shared.response.Response;

import java.security.Principal;
import java.util.List;

public interface QueryMenuService {

    List<RichMenu> getAllMenusBelongingToUser(Principal user);

    Response<RichMenu> findById(Long menuId, Principal user) throws NotFoundException, NotAuthorizedException;

    Response<FullMenu> getFullMenu(Long menuId, Principal user) throws NotFoundException, NotAuthorizedException;

}
