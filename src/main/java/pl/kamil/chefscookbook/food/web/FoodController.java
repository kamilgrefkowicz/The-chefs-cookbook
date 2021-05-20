package pl.kamil.chefscookbook.food.web;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.QueryItemWithDependenciesCommand;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.security.UserSecurity;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/food")
public class FoodController {

    private final QueryItemUseCase queryItem;
    private final UserSecurity userSecurity;

    @Secured({"ROLE_USER"})
    @GetMapping({"/", "/my-items"})
    public String showMyItems(Model model, Principal user) {
        model.addAttribute(queryItem.findAllItemsBelongingToUser(user));
        model.addAttribute("command", new QueryItemWithDependenciesCommand());
        return "/food/my-items";
    }

    @GetMapping("/view-item")
    public String showItem(Model model, @Valid QueryItemWithDependenciesCommand command, BindingResult bindingResult, Principal user) {

        if (command.getTargetAmount() == null || bindingResult.hasErrors()) {
            command.setTargetAmount(BigDecimal.ONE);
        }

        model.addAttribute("command", command);
        RichItem item = queryItem.findById(command.getItemId());

        if (!userSecurity.isOwner(item.getUserEntityId(), user)) {
            model.addAttribute("error", "You're not authorized to view this item.");
            return "/error";
        }

        model.addAttribute("targetItem", item);



        Map<PoorItem, BigDecimal> basics = new LinkedHashMap<>();
        Map<RichItem, BigDecimal> intermediates = new LinkedHashMap<>();


        Map<ItemDto, BigDecimal> mapOfAllDependencies;

        mapOfAllDependencies = queryItem.getMapOfAllDependencies(command);


        splitMapToBasicsAndIntermediates(mapOfAllDependencies, basics, intermediates);
        model.addAttribute("targetAmount", command.getTargetAmount());
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
