package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;

import javax.persistence.Access;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.menu.application.dto.MenuDto.convertToPoorMenu;

@Service
@AllArgsConstructor
public class QueryMenu implements QueryMenuUseCase {

    private final MenuRepository menuRepository;

    @Override
    public List<PoorMenu> getAllMenusBelongingToUser(Principal user) {
        return menuRepository.findAllByUserEntityId(Long.valueOf(user.getName()))
                .stream()
                .map(MenuDto::convertToPoorMenu)
                .collect(Collectors.toList());
    }
}
