package pl.kamil.chefscookbook.food.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase.*;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.kamil.chefscookbook.food.domain.staticData.Unit.unitList;
import static pl.kamil.chefscookbook.shared.url_values.UrlValueHolder.*;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class ModifyFoodController {

    private final ModifyItemUseCase modifyItem;
    private final QueryItemUseCase queryItem;



    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("createNewItemCommand", new CreateNewItemCommand());
        model.addAttribute("units", unitList());
        model.addAttribute("addIngredientCommand", new AddIngredientCommand());
        model.addAttribute("removeIngredientCommand", new RemoveIngredientFromRecipeCommand());
        model.addAttribute("setYieldCommand", new SetYieldCommand());
        model.addAttribute("updateDescriptionCommand", new UpdateDescriptionCommand());
        model.addAttribute("deleteItemCommand", new DeleteItemCommand());
    }

    @GetMapping("/modify-item")
    public String showModifyItemForm(Model model, @RequestParam Long itemId, Principal user){
        Response<RichItem> queried = queryItem.findById(itemId, user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem item = queried.getData();
        model.addAttribute("item", item);
        return ITEM_MODIFY;
    }

    @GetMapping("/new-item")
    public String showCreateNewItemForm() {
        return ITEM_CREATE;
    }

    @PostMapping("/new-item")
    public String createNewItem(Model model, @Valid CreateNewItemCommand command, BindingResult result, Principal user) {

        if (result.hasErrors()) return ITEM_CREATE;

        Response<ItemDto> itemCreated = modifyItem.createItem(command, user);

        if (!itemCreated.isSuccess()) {
            model.addAttribute(ERROR, itemCreated.getError());
            return ITEM_CREATE;
        }
        model.addAttribute("item", itemCreated.getData());
        return ITEM_MODIFY;
    }


    @PostMapping("/add-ingredient")
    public String addIngredient(Model model, @Valid AddIngredientCommand command, BindingResult bindingResult, Principal user) {
        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem item = queried.getData();

        if (!validationSuccessful(bindingResult, model, item)) return ITEM_MODIFY;

        Response<RichItem> modification = modifyItem.addIngredientToRecipe(command, user);

        resolveModification(modification, model, item);

        return ITEM_MODIFY;

    }

    @PostMapping("/remove-ingredient")
    public String removeIngredient(Model model, RemoveIngredientFromRecipeCommand command, Principal user) {

        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem item = queried.getData();

        Response<RichItem> modification = modifyItem.removeIngredientFromRecipe(command, user);

        resolveModification(modification, model, item);
        return ITEM_MODIFY;
    }

    @PostMapping("/set-yield")
    public String setYield(Model model, @Valid SetYieldCommand command, BindingResult bindingResult, Principal user) {

        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);
        if (!querySuccessful(queried, model)) return ERROR;

        RichItem item = queried.getData();

        if (!validationSuccessful(bindingResult, model, item)) return ITEM_MODIFY;

        Response<RichItem> modification = modifyItem.setYield(command, user);

        resolveModification(modification, model, item);

        return ITEM_MODIFY;
    }


    @PostMapping("/modify-description")
    public String updateDescription(Model model, @Valid UpdateDescriptionCommand command, BindingResult bindingResult, Principal user) {

        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem item = queried.getData();

        if (!validationSuccessful(bindingResult, model, item)) return ITEM_MODIFY;

        Response<RichItem> modification = modifyItem.updateDescription(command, user);

        resolveModification(modification, model, item);
        return ITEM_MODIFY;
    }
    @GetMapping("/delete-item")
    public String showConfirmPageForDelete(Model model, DeleteItemCommand command, Principal user) {
        Response<RichItem> queried = queryItem.findById(command.getItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        ItemDto targetItem = queried.getData();
        List<PoorItem> itemsAffected = queryItem.findAllItemsAffectedByDelete(command.getItemId());

        model.addAttribute("itemsAffected", itemsAffected);
        model.addAttribute("targetItem", targetItem);
        model.addAttribute("command", command);

        return ITEM_DELETE_CONFIRM;
    }
    @PostMapping("/delete-item")
    public String deleteItem(Model model, DeleteItemCommand command, Principal user) {
        Response<RichItem> queried = queryItem.findById(command.getItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        modifyItem.deleteItem(command);
        model.addAttribute(queryItem.findAllItemsBelongingToUser(user));


        return ITEMS_LIST;
    }

    private boolean querySuccessful(Response<RichItem> response, Model model) {
        if (!response.isSuccess()) {
            model.addAttribute(ERROR, response.getError());
            return false;
        }
        return true;
    }

    private boolean validationSuccessful(BindingResult bindingResult, Model model, RichItem item) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("item", item);
            return false;
        }
        return true;
    }

    private void resolveModification(Response<RichItem> modification, Model model, RichItem item) {
        if (!modification.isSuccess()) {
            model.addAttribute(ERROR, modification.getError());
            model.addAttribute("item", item);
        } else {
            model.addAttribute("item", modification.getData());
        }
    }
}
