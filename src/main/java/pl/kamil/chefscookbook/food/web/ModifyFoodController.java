package pl.kamil.chefscookbook.food.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.*;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.shared.controller.ValidatedController;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.*;

@Controller
@AllArgsConstructor
@RequestMapping("/food")
public class ModifyFoodController extends ValidatedController<RichItem> {

    private final ModifyItemService modifyItem;
    private final QueryItemService queryItem;



    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("createNewItemCommand", new CreateNewItemCommand());
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

        RichItem object = queried.getData();
        model.addAttribute("object", object);
        return ITEM_MODIFY;
    }

    @GetMapping("/new-item")
    public String showCreateNewItemForm() {
        return ITEM_CREATE;
    }

    @PostMapping("/new-item")
    public String createNewItem(Model model, @Valid CreateNewItemCommand command, BindingResult result, Principal user, @RequestParam(required = false) Long itemId) {

        if (result.hasErrors()) return ITEM_CREATE;

        Response<ItemDto> itemCreated = modifyItem.createItem(command, user);

        if (!itemCreated.isSuccess()) {
            model.addAttribute(ERROR, itemCreated.getError());
            return ITEM_CREATE;
        }
        if (itemId == null && command.getType() == Type.BASIC) {
            return ITEM_CREATE;
        }
        ItemDto object;

        if (itemId != null) {
            object = queryItem.findById(itemId, user).getData();
        } else {
            object = itemCreated.getData();
        }


        model.addAttribute("object", object);
        return ITEM_MODIFY;
    }


    @PostMapping("/add-ingredient")
    public String addIngredient(Model model, @Valid AddIngredientCommand command, BindingResult bindingResult, Principal user) {
        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem object = queried.getData();

        if (!validationSuccessful(bindingResult, model, object)) return ITEM_MODIFY;

        Response<RichItem> modification = modifyItem.addIngredientToRecipe(command, user);

        resolveModification(modification, model, object);

        return ITEM_MODIFY;

    }

    @PostMapping("/remove-ingredient")
    public String removeIngredient(Model model, RemoveIngredientFromRecipeCommand command, Principal user) {

        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem object = queried.getData();

        Response<RichItem> modification = modifyItem.removeIngredientFromRecipe(command, user);

        resolveModification(modification, model, object);
        return ITEM_MODIFY;
    }

    @PostMapping("/set-yield")
    public String setYield(Model model, @Valid SetYieldCommand command, BindingResult bindingResult, Principal user) {

        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);
        if (!querySuccessful(queried, model)) return ERROR;

        RichItem object = queried.getData();

        if (!validationSuccessful(bindingResult, model, object)) return ITEM_MODIFY;

        Response<RichItem> modification = modifyItem.setYield(command, user);

        resolveModification(modification, model, object);

        return ITEM_MODIFY;
    }


    @PostMapping("/modify-description")
    public String updateDescription(Model model, @Valid UpdateDescriptionCommand command, BindingResult bindingResult, Principal user) {

        Response<RichItem> queried = queryItem.findById(command.getParentItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        RichItem object = queried.getData();

        if (!validationSuccessful(bindingResult, model, object)) return ITEM_MODIFY;

        Response<RichItem> modification = modifyItem.updateDescription(command, user);

        resolveModification(modification, model, object);
        return ITEM_MODIFY;
    }
    @GetMapping("/delete-item")
    public String showConfirmPageForDelete(Model model, DeleteItemCommand command, Principal user) {
        Response<RichItem> queried = queryItem.findById(command.getItemId(), user);

        if (!querySuccessful(queried, model)) return ERROR;

        ItemDto object = queried.getData();
        List<PoorItem> itemsAffected = queryItem.findAllItemsAffectedByDelete(command.getItemId());

        model.addAttribute("itemsAffected", itemsAffected);
        model.addAttribute("object", object);

        return ITEM_DELETE_CONFIRM;
    }
    @PostMapping("/delete-item")
    public String deleteItem(Model model, DeleteItemCommand command, Principal user) {

        modifyItem.deleteItem(command, user);
        model.addAttribute(queryItem.findAllItemsBelongingToUser(user));

        return ITEMS_LIST;
    }


}
