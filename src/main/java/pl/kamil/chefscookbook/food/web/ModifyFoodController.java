package pl.kamil.chefscookbook.food.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.CreateNewItemCommand;

import java.security.Principal;

import static pl.kamil.chefscookbook.food.domain.staticData.Unit.unitList;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class ModifyFoodController {


    @GetMapping("/new-item")
    public String showCreateNewItemForm(Model model) {
        model.addAttribute(new CreateNewItemCommand());
        model.addAttribute("units", unitList());
        return "food/modify-items/create-item";
    }
}
