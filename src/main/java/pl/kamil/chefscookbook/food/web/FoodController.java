package pl.kamil.chefscookbook.food.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.domain.staticData.Type;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

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
    public String showItem(Model model, @RequestParam Long itemId, @RequestParam(required = false, defaultValue = "1") BigDecimal targetAmount) {
        Map<ItemDto, BigDecimal> mapOfAllDependencies = queryItem.getMapOfAllDependencies(itemId, targetAmount);

        Map<PoorItem, BigDecimal> basics = new LinkedHashMap<>();
        Map<RichItem, BigDecimal> intermediates = new LinkedHashMap<>();

        splitMapToBasicsAndIntermediates(mapOfAllDependencies, basics, intermediates);


        model.addAttribute("targetItem", queryItem.findById(itemId));
        model.addAttribute("targetAmount", targetAmount);
        model.addAttribute("basics", basics);
        model.addAttribute("intermediates", intermediates);
        return "/food/view-item";
    }

    private void splitMapToBasicsAndIntermediates(Map<ItemDto, BigDecimal> mapOfAllDependencies, Map<PoorItem, BigDecimal> basics, Map<RichItem, BigDecimal> intermediates) {
        for (Map.Entry<ItemDto, BigDecimal> entry : mapOfAllDependencies.entrySet()) {
            if (entry.getKey().getType().equals(Type.BASIC())) basics.put((PoorItem) entry.getKey(), entry.getValue());
            else intermediates.put((RichItem) entry.getKey(), entry.getValue());
        }
    }
}
