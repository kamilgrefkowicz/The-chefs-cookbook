package pl.kamil.chefscookbook.menu.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.application.port.QueryItemService.QueryItemWithDependenciesCommand;
import pl.kamil.chefscookbook.menu.application.dto.RichMenu;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.AddItemsToMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.CreateNewMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.DeleteMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.ModifyMenuService.RemoveItemFromMenuCommand;
import pl.kamil.chefscookbook.menu.application.port.QueryMenuService;
import pl.kamil.chefscookbook.shared.controller.ValidatedController;
import pl.kamil.chefscookbook.shared.exceptions.NotAuthorizedException;
import pl.kamil.chefscookbook.shared.exceptions.NotFoundException;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.*;

@Controller
@AllArgsConstructor
@RequestMapping("/menu")
public class MenuController extends ValidatedController<RichMenu> {

    private final ModifyMenuService modifyMenu;
    private final QueryMenuService queryMenu;
    private final QueryItemService queryItem;


    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("queryItemCommand", new QueryItemWithDependenciesCommand());
        model.addAttribute("createNewMenuCommand", new CreateNewMenuCommand());
        model.addAttribute("addItemsCommand", new AddItemsToMenuCommand());
        model.addAttribute("removeItemFromMenuCommand", new RemoveItemFromMenuCommand());
        model.addAttribute("deleteMenuCommand", new DeleteMenuCommand());
    }

    @GetMapping("/my-menus")
    public String showMyMenus(Model model, Principal user) {
        List<RichMenu> menuList = queryMenu.getAllMenusBelongingToUser(user);
        model.addAttribute("menuList", menuList);
        return MENU_LIST;
    }

    @GetMapping("/new-menu")
    public String showNewMenuForm() {
        return MENU_CREATE;
    }

    @PostMapping("/new-menu")
    public String createNewMenu(Model model, @Valid CreateNewMenuCommand command, BindingResult result, Principal user) {
        if (result.hasErrors()) return MENU_CREATE;

        Response<RichMenu> menuCreated = modifyMenu.createNewMenu(command, user);

        if (!menuCreated.isSuccess()) {
            model.addAttribute("error", menuCreated.getError());
            return MENU_CREATE;
        }

        model.addAttribute("object", menuCreated.getData());
        return MENU_VIEW;
    }

    @GetMapping("/view-menu")
    public String showMenu(Model model, @RequestParam Long menuId, Principal user) throws NotFoundException, NotAuthorizedException {

        Response<RichMenu> queried = queryMenu.findById(menuId, user);

        model.addAttribute("object", queried.getData());

        return MENU_VIEW;
    }


    @GetMapping("/add-items")
    public String showAddItemToMenuForm(Model model, @RequestParam Long menuId, Principal user) throws NotFoundException, NotAuthorizedException {

        Response<RichMenu> queried = queryMenu.findById(menuId, user);

        List<PoorItem> dishes = queryItem.findAllEligibleDishesForMenu(user, menuId);

        model.addAttribute("object", queried.getData());
        model.addAttribute("dishes", dishes);

        return MENU_ADD_ITEMS;

    }

    @PostMapping("/add-items")
    public String addItemsToMenu(Model model, AddItemsToMenuCommand command, Principal user) throws NotFoundException, NotAuthorizedException {

        Response<RichMenu> queried = queryMenu.findById(command.getMenuId(), user);

        Response<RichMenu> modification = modifyMenu.addItemsToMenu(command, user);

        resolveModification(modification, model, queried.getData());

        return MENU_VIEW;
    }

    @PostMapping("/remove-item")
    public String removeItemFromMenu(Model model, RemoveItemFromMenuCommand command, Principal user) throws NotFoundException, NotAuthorizedException {

        Response<RichMenu> queried = queryMenu.findById(command.getMenuId(), user);

        Response<RichMenu> modification = modifyMenu.removeItemFromMenu(command, user);

        resolveModification(modification, model, queried.getData());

        return MENU_VIEW;

    }
    @GetMapping("/delete-menu")
    public String showDeleteMenuConfirmation(Model model, DeleteMenuCommand command, Principal user) throws NotFoundException, NotAuthorizedException {
        Response<RichMenu> queried = queryMenu.findById(command.getMenuId(), user);
        
        model.addAttribute("object", queried.getData());

        return MENU_DELETE_CONFIRM;
    }
    @PostMapping("/delete-menu")
    public String deleteMenu(Model model, DeleteMenuCommand command, Principal user) {
        modifyMenu.deleteMenu(command, user);

        model.addAttribute("menuList", queryMenu.getAllMenusBelongingToUser(user));
        return MENU_LIST;
    }
}
