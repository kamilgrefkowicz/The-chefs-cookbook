package pl.kamil.chefscookbook.food.web;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.CreateNewItemCommand;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.DeleteItemCommand;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.application.port.QueryItemService.QueryItemWithDependenciesCommand;
import pl.kamil.chefscookbook.shared.controller.ValidatedController;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/food")
public class ViewFoodController extends ValidatedController<ItemDto> {

    private final QueryItemService queryItem;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("queryItemCommand", new QueryItemWithDependenciesCommand());
    }

    @GetMapping({"/", "/my-items"})
    public String showMyItems(Model model, Principal user) {
        model.addAttribute(queryItem.findAllItemsBelongingToUser(user));
        model.addAttribute("deleteItemCommand", new DeleteItemCommand());

        return ITEMS_LIST;
    }

    @SneakyThrows
    @GetMapping("/view-item")
    public String showItem(Model model, @Valid QueryItemWithDependenciesCommand command, BindingResult bindingResult, Principal user)  {

        if (bindingResult.hasErrors()) {
            command.setTargetAmount(BigDecimal.ONE);
        }

        Response<ItemDto> queried = queryItem.findById(command.getItemId(), user);

        model.addAttribute("object", queried.getData());
        model.addAttribute("targetAmount", command.getTargetAmount());

        addDependencyMapsToModel(model, command);

        return ITEM_VIEW;
    }
    @GetMapping("/view-basics")
    public String showAllBasicItems(Model model, Principal user) {
        List<PoorItem> basics = queryItem.findAllBasicsForUser(user);
        model.addAttribute("basics", basics);
        model.addAttribute("createNewItemCommand", new CreateNewItemCommand());
        return BASIC_ITEMS_VIEW;
    }


    // this splitting of maps is strongly connected to how the result is presented in thymeleaf view
    // which is why this piece of logic stays in controller
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
            if (entry.getKey().getType().equals(BASIC)) basics.put((PoorItem) entry.getKey(), entry.getValue());
            else intermediates.put((RichItem) entry.getKey(), entry.getValue());
        }
    }
}
