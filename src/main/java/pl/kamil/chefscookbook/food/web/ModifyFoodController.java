package pl.kamil.chefscookbook.food.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.RemoveIngredientFromRecipeCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.UserSecurityService;

import javax.validation.Valid;
import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Unit.unitList;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class ModifyFoodController {

    private final ModifyItemUseCase modifyItem;
    private final QueryItemUseCase queryItem;
    private final UserSecurityService userSecurity;

    @GetMapping("/new-item")
    public String showCreateNewItemForm(Model model) {
        model.addAttribute(new CreateNewItemCommand());
        model.addAttribute("units", unitList());
        return "food/modify-items/create-item";
    }

    @PostMapping("/new-item")
    public String createNewItem(Model model, @Valid CreateNewItemCommand command, Principal user) {

        command.setUserId(Long.valueOf(user.getName()));

        Response<ItemDto> itemCreated = modifyItem.createItem(command);

        if (!itemCreated.isSuccess()) {
            model.addAttribute("units", unitList());
            model.addAttribute("message", itemCreated.getError());
            return "food/modify-items/create-item";
        }
        model.addAttribute("item", itemCreated.getData());
        model.addAttribute("addIngredientCommand", new AddIngredientCommand());

        return "food/modify-items/modify-ingredients";
    }

    @PostMapping("/add-ingredient")
    public String addIngredient(Model model, @Valid AddIngredientCommand command, BindingResult result, Principal user) {
        RichItem item = queryItem.findById(command.getParentItemId());

        model.addAttribute("addIngredientCommand", new AddIngredientCommand());
        model.addAttribute("removeIngredientCommand", new RemoveIngredientFromRecipeCommand());

        if (result.hasErrors()) {
            model.addAttribute(item);
            return "food/modify-items/modify-ingredients";
        }

        Response<RichItem> response = modifyItem.addIngredientToRecipe(command, Long.valueOf(user.getName()));

        if (response.isSuccess()) {
            item = response.getData();
        } else {
            model.addAttribute("message", response.getError());
        }

        model.addAttribute("item", item);

        return "food/modify-items/modify-ingredients";

    }
    @PostMapping("/remove-ingredient")
    public String removeIngredient(Model model, RemoveIngredientFromRecipeCommand command, Principal user) {

        if (!userSecurity.isOwner(command.getParentItemId(), user)) {
            model.addAttribute("message", "You're not authorized to modify this item");
            return "/error";
        }
        RichItem item = modifyItem.removeIngredientFromRecipe(command);

        model.addAttribute("item", item);
        model.addAttribute("addIngredientCommand", new AddIngredientCommand());
        model.addAttribute("removeIngredientCommand", new RemoveIngredientFromRecipeCommand());

        return "food/modify-items/modify-ingredients";
    }
}
