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
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;
import pl.kamil.chefscookbook.shared.response.Response;

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

        Response<ItemDto> itemCreated = modifyItem.createItem(command);

        if (!itemCreated.isSuccess()) {
            model.addAttribute("units", unitList());
            model.addAttribute("message", itemCreated.getError());
            return "food/modify-items/create-item";
        }
        model.addAttribute("item", itemCreated.getData());
        model.addAttribute("command", new AddIngredientCommand());

        return "food/modify-items/modify-ingredients";
    }

    @PostMapping("add-ingredient")
    public String addIngredient(Model model, @Valid AddIngredientCommand command, BindingResult result, Principal user) {

        if (result.hasErrors()) return "food/modify-items/modify-ingredients";

        RichItem item = queryItem.findById(command.getParentItemId());

        Response<RichItem> response = modifyItem.addIngredientToRecipe(command, Long.valueOf(user.getName()));

        if (response.isSuccess()) {
            item = response.getData();
        } else {
            model.addAttribute("message", response.getError());
        }

        model.addAttribute("item", item);
        model.addAttribute("command", new AddIngredientCommand());

        return "food/modify-items/modify-ingredients";

    }
}
