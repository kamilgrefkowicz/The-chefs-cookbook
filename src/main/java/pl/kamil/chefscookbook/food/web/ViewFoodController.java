package pl.kamil.chefscookbook.food.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.DeleteItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.QueryItemWithDependenciesCommand;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.UserSecurityService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import static pl.kamil.chefscookbook.shared.url_values.UrlValueHolder.ERROR;
import static pl.kamil.chefscookbook.shared.url_values.UrlValueHolder.ITEM_VIEW;

@Controller
@RequiredArgsConstructor
@RequestMapping("/food")
public class ViewFoodController {

    private final QueryItemUseCase queryItem;

    @GetMapping({"/", "/my-items"})
    public String showMyItems(Model model, Principal user) {
        model.addAttribute(queryItem.findAllItemsBelongingToUser(user));
        model.addAttribute("command", new QueryItemWithDependenciesCommand());
        model.addAttribute("deleteItemCommand", new DeleteItemCommand());

        return "/food/my-items";
    }

    @GetMapping("/view-item")
    public String showItem(Model model, @Valid QueryItemWithDependenciesCommand command, BindingResult bindingResult, Principal user) {

        if (command.getTargetAmount() == null || bindingResult.hasErrors()) {
            command.setTargetAmount(BigDecimal.ONE);
        }

        model.addAttribute("command", command);
        Response<RichItem> queried = queryItem.findById(command.getItemId(), user);

        if (!queried.isSuccess()) {
            model.addAttribute("error", queried.getError());
            return ERROR;
        }

        model.addAttribute("targetItem", queried.getData());
        model.addAttribute("targetAmount", command.getTargetAmount());

        addDependencyMapsToModel(model, command);

        return ITEM_VIEW;


    }

    private void addDependencyMapsToModel(Model model, QueryItemWithDependenciesCommand command) {
        Map<PoorItem, BigDecimal> basics = new LinkedHashMap<>();
        Map<RichItem, BigDecimal> intermediates = new LinkedHashMap<>();

        Map<ItemDto, BigDecimal> mapOfAllDependencies = queryItem.getMapOfAllDependencies(command);

        splitMapToBasicsAndIntermediates(mapOfAllDependencies, basics, intermediates);
        model.addAttribute("basics", basics);
        model.addAttribute("intermediates", intermediates);
    }

    private void splitMapToBasicsAndIntermediates(Map<ItemDto, BigDecimal> mapOfAllDependencies, Map<PoorItem, BigDecimal> basics, Map<RichItem, BigDecimal> intermediates) {
        for (Map.Entry<ItemDto, BigDecimal> entry : mapOfAllDependencies.entrySet()) {
            if (entry.getKey().getType().equals(Type.BASIC())) basics.put((PoorItem) entry.getKey(), entry.getValue());
            else intermediates.put((RichItem) entry.getKey(), entry.getValue());
        }
    }
}
