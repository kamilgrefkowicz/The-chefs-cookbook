package pl.kamil.chefscookbook.menu.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.menu.application.dto.FullMenu;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuUseCase;
import pl.kamil.chefscookbook.menu.database.MenuRepository;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.DISH;
import static pl.kamil.chefscookbook.menu.application.dto.RichMenu.convertToRichMenu;


@Service
@AllArgsConstructor
public class QueryMenu implements QueryMenuUseCase {

    private final MenuRepository menuRepository;
    private final UserSecurityUseCase userSecurity;

    @Override
    @Transactional
    public List<RichMenu> getAllMenusBelongingToUser(Principal user) {
        return menuRepository.findAllByUserEntityId(Long.valueOf(user.getName()))
                .stream()
                .map(RichMenu::convertToRichMenu)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Response<MenuDto> findById(Long menuId, Principal user, boolean getFullMenu) {
        Optional<Menu> opt = menuRepository.findById(menuId);

        if (opt.isEmpty()) return Response.failure("Menu o id " + menuId + " nie istnieje");

        Menu menu = opt.get();
        if (!userSecurity.isOwner(menu.getUserEntity().getId(), user))
            return Response.failure("You do not own this menu");

        if (!getFullMenu) return Response.success(convertToRichMenu(menu));

        return Response.success(convertToFullMenu(menu));
    }

    private FullMenu convertToFullMenu(Menu menu) {
        Set<Item> allDishesAndDependencies = new HashSet<>();

        Set<RichItem> dishes = new HashSet<>();
        Set<RichItem> intermediates = new HashSet<>();
        Set<PoorItem> basics = new HashSet<>();

        menu.getItems().forEach(item -> getAllDependencies(item, allDishesAndDependencies));

        allDishesAndDependencies.forEach(item -> placeItemInAppropriateSet(item, dishes, intermediates, basics));

        return new FullMenu(menu, dishes, intermediates, basics);
    }

    private void getAllDependencies(Item item, Set<Item> allDishesAndDependencies) {
        allDishesAndDependencies.add(item);
        if (!item.getType().equals(BASIC())) {
            item.getIngredients().forEach(ingredient -> getAllDependencies(ingredient.getChildItem(), allDishesAndDependencies));
        }
    }


    private void placeItemInAppropriateSet(Item item, Set<RichItem> dishes, Set<RichItem> intermediates, Set<PoorItem> basics) {
        switch (item.getType().getId()) {
            case 1:
                basics.add(new PoorItem(item));
                return;
            case 2:
                intermediates.add(new RichItem(item));
                return;
            case 3:
                dishes.add(new RichItem(item));
                return;
            default:
                throw new IllegalArgumentException();
        }
    }



}
