package pl.kamil.chefscookbook.food.application.port;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.security.core.userdetails.User;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.shared.response.Response;


import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface QueryItemUseCase {

    Response<RichItem> findById(Long itemId, Principal user);

    Map<ItemDto, BigDecimal> getMapOfAllDependencies(QueryItemWithDependenciesCommand command);

    List<PoorItem> findAllItemsBelongingToUser(Principal user);

    List<ItemAutocompleteDto> findForAutocomplete(String term, Long userId);


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class QueryItemWithDependenciesCommand {

        @NotNull
        Long itemId;

        @Digits(integer = 4, fraction = 2)
        @DecimalMin(value = "0", inclusive = false)
        BigDecimal targetAmount;
    }


}











