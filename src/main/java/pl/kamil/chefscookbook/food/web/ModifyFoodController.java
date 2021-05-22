package pl.kamil.chefscookbook.food.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;

import javax.validation.Valid;
import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Unit.unitList;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class ModifyFoodController {

    private final ModifyItemUseCase modifyItem;

    @GetMapping("/new-item")
    public String showCreateNewItemForm(Model model) {
        model.addAttribute(new CreateNewItemCommand());
        model.addAttribute("units", unitList());
        return "food/modify-items/create-item";
    }

    @PostMapping("/new-item")
    public String createNewItem(Model model, @Valid CreateNewItemCommand command, Principal user) {

        command.setUserId(Long.valueOf(user.getName()));

        try {
            modifyItem.createItem(command);
        } catch (NameAlreadyTakenException e) {
            model.addAttribute("units", unitList());
            model.addAttribute("message", e.getMessage());
            return "food/modify-items/create-item";
        }

        return "food/modify-items/modify-ingredients";
    }
}
