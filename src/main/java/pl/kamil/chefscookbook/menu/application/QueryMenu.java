package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class QueryMenu implements QueryMenuUseCase {

    private final MenuRepository menuRepository;

    @Override
    public List<RichMenu> getAllMenusBelongingToUser(Principal user) {
        return menuRepository.findAllByUserEntityId(Long.valueOf(user.getName()))
                .stream()
                .map(RichMenu::convertToRichMenu)
                .collect(Collectors.toList());
    }
}
