package pl.kamil.chefscookbook.menu.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuUseCase;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuUseCase.AddItemsToMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuUseCase.CreateNewMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuUseCase;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController {

    private final ModifyMenuUseCase modifyMenu;
    private final QueryMenuUseCase queryMenu;
    private final QueryItemUseCase queryItem;

    private final String MENU_NEW = "/menu/new-menu";
    private final String MENU_LIST = "/menu/my-menus";
    private final String MENU_VIEW = "/menu/view-menu";
    private final String MENU_ADD_ITEMS = "/menu/add-items";

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("createNewMenuCommand", new CreateNewMenuCommand());
        model.addAttribute("addItemsCommand", new AddItemsToMenuCommand());
    }

    @GetMapping("/my-menus")
    public String showMyMenus(Model model, Principal user) {
        List<PoorMenu> menuList = queryMenu.getAllMenusBelongingToUser(user);
        model.addAttribute("menuList", menuList);
        return MENU_LIST;
    }

    @GetMapping("/new-menu")
    public String showNewMenuForm() {
        return MENU_NEW;
    }

    @PostMapping ("/new-menu")
    public String createNewMenu(Model model, @Valid CreateNewMenuCommand command, BindingResult result, Principal user) {
        if (result.hasErrors()) return MENU_NEW;

        Response<PoorMenu> menuCreated = modifyMenu.createNewMenu(command, user);

        if (!menuCreated.isSuccess()) {
            model.addAttribute("error", menuCreated.getError());
            return MENU_NEW;
        }

        model.addAttribute("menu", menuCreated.getData());
        return MENU_VIEW;
    }
    @GetMapping("/add-items")
    public String showAddItemToMenuForm(Model model, @RequestParam Long menuId, Principal user) {

        List<PoorItem> dishes = queryItem.findAllEligibleDishesForMenu(user, menuId);

        model.addAttribute("dishes", dishes);
        model.addAttribute("menuId", menuId);

        return MENU_ADD_ITEMS;

    }
}
