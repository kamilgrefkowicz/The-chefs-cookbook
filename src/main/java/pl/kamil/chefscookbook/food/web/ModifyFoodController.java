package pl.kamil.chefscookbook.food.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.AddIngredientCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.shared.exception.LoopAttemptedException;
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;

import javax.validation.Valid;
import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Unit.unitList;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class ModifyFoodController {

    private final ModifyItemUseCase modifyItem;
    private final QueryItemUseCase queryItem;

    @GetMapping("/new-item")
    public String showCreateNewItemForm(Model model) {
        model.addAttribute(new CreateNewItemCommand());
        model.addAttribute("units", unitList());
        return "food/modify-items/create-item";
    }

    @PostMapping("/new-item")
    public String createNewItem(Model model, @Valid CreateNewItemCommand command, Principal user) {

        command.setUserId(Long.valueOf(user.getName()));

            ItemDto item;
        try {
            item = modifyItem.createItem(command);
        } catch (NameAlreadyTakenException e) {
            model.addAttribute("units", unitList());
            model.addAttribute("message", e.getMessage());
            return "food/modify-items/create-item";
        }
        model.addAttribute("item", item);
        model.addAttribute("command", new AddIngredientCommand());

        return "food/modify-items/modify-ingredients";
    }

    @PostMapping
    public String addIngredient(Model model, @Valid AddIngredientCommand command, Principal user) {

        RichItem item = queryItem.findById(command.getParentItemId());
        try {
            item = modifyItem.addIngredientToRecipe(command, Long.valueOf(user.getName()));
        } catch (LoopAttemptedException e) {
            model.addAttribute("message", e.getMessage());
        }
        model.addAttribute("item", item);
        model.addAttribute("command", new AddIngredientCommand());

        return "food/modify-items/modify-ingredients";

    }
}
