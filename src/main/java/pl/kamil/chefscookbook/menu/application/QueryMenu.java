package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuService;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.exceptions.NotAuthorizedException;
import pl.kamil.chefscookbook.shared.exceptions.NotFoundException;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;


@Service
@AllArgsConstructor
public class QueryMenu implements QueryMenuService {

    private final MenuRepository menuRepository;
    private final UserSecurityService userSecurity;

    @Override
    @Transactional
    public List<RichMenu> getAllMenusBelongingToUser(Principal user) {
        return menuRepository.findAllByUserEntityId(Long.valueOf(user.getName()))
                .stream()
                .map(RichMenu::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Response<RichMenu> findById(Long menuId, Principal user) {

       Menu menu = authorize(menuId, user);

        return Response.success(new RichMenu(menu));

    }

    @Override
    public Response<FullMenu> getFullMenu(Long menuId, Principal user) {
        Menu menu = authorize(menuId, user);

        return Response.success(convertToFullMenu(menu));
    }

    @SneakyThrows
    private Menu authorize (Long menuId, Principal user) {
        Optional<Menu> optionalMenu = menuRepository.findById(menuId);
        if (optionalMenu.isEmpty()) throw new NotFoundException();

        Menu menu = optionalMenu.get();
        if (!userSecurity.belongsTo(menu, user)) throw new NotAuthorizedException();

        return menu;
    }

    private FullMenu convertToFullMenu(Menu menu) {
        Set<Item> allDishesAndDependencies = new HashSet<>();

        Set<RichItem> dishes = new HashSet<>();
        Set<RichItem> intermediates = new HashSet<>();
        Set<PoorItem> basics = new HashSet<>();

        menu.getItems().forEach(item -> getAllDependencies(item, allDishesAndDependencies));

        //it's somehow tidier when done in two passes ;)
        allDishesAndDependencies.forEach(item -> placeItemInAppropriateSet(item, dishes, intermediates, basics));

        return new FullMenu(menu, dishes, intermediates, basics);
    }

    private void getAllDependencies(Item item, Set<Item> allDishesAndDependencies) {
        allDishesAndDependencies.add(item);
        if (canContinueRecursion(item)) {
            item.getIngredients().forEach(ingredient -> getAllDependencies(ingredient.getChildItem(), allDishesAndDependencies));
        }
    }

    private boolean canContinueRecursion(Item item) {
        return !item.getType().equals(BASIC);
    }


    private void placeItemInAppropriateSet(Item item, Set<RichItem> dishes, Set<RichItem> intermediates, Set<PoorItem> basics) {
        switch (item.getType()) {
            case BASIC:
                basics.add(new PoorItem(item));
                return;
            case INTERMEDIATE:
                intermediates.add(new RichItem(item));
                return;
            case DISH:
                dishes.add(new RichItem(item));
                return;
            default:
                throw new IllegalArgumentException();
        }
    }



}
