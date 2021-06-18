package pl.kamil.chefscookbook.food.application.port;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface QueryItemService {

    Response<RichItem> findById(Long itemId, Principal user);

    Map<ItemDto, BigDecimal> getMapOfAllDependencies(QueryItemWithDependenciesCommand command);

    List<PoorItem> findAllItemsBelongingToUser(Principal user);

    List<ItemAutocompleteDto> findForAutocomplete(String term, Principal user);

    List<PoorItem> findAllItemsAffectedByDelete(Long itemId);

    List<PoorItem> findAllEligibleDishesForMenu(Principal user, Long menuId);

    List<PoorItem> findAllBasicsForUser(Principal user);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class QueryItemWithDependenciesCommand {

        Long itemId;

        @Digits(integer = 4, fraction = 2)
        @DecimalMin(value = "0", inclusive = false)
        @DecimalMax(value = "10")
        BigDecimal targetAmount;
    }


}











