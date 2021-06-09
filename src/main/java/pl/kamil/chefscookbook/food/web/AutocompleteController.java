package pl.kamil.chefscookbook.food.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/autocomplete")
public class AutocompleteController {

    private final QueryItemService queryItem;

    @GetMapping
    @ResponseBody
    public List<ItemAutocompleteDto> getPossibleIngredients(@RequestParam String term, Principal user) {

        return queryItem.findForAutocomplete(term, Long.valueOf(user.getName()));
    }
}
