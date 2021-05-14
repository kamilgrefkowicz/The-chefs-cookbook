package pl.kamil.chefscookbook.food.application.port;


import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface QueryItemUseCase {
    List<PoorItem> findAll();

    RichItem findById(Long id);

    Map<ItemDto, BigDecimal> getMapOfAllDependencies(Long itemId, BigDecimal targetAmount);




}











