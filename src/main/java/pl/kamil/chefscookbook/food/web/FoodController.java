package pl.kamil.chefscookbook.food.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kamil.chefscookbook.food.application.QueryItemService;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {

    private final QueryItemUseCase queryItem;

    @GetMapping({"/","/my-items"})
    public String showMyItems(Model model) {
        model.addAttribute(queryItem.findAll());
        return "/food/my-items";
    }

    @GetMapping("/view-item")
    public String showItem(Model model, @RequestParam Long itemId, @RequestParam(required = false, defaultValue = "1") BigDecimal target) {
        model.addAttribute(queryItem.findById(itemId));
        model.addAttribute("target", target);
        model.addAttribute("dependencies", queryItem.getMapOfAllDependencies(itemId, target));
        return "/food/view-item";
    }
}
