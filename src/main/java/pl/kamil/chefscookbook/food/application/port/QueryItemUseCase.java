package pl.kamil.chefscookbook.food.application.port;


import lombok.Builder;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import java.math.BigDecimal;
import java.util.List;

public interface QueryItemUseCase {
    List<PoorItem> findAll();


    @Value
    @Builder
    class PoorItem {
        Long id;
        String name;
        Unit unit;
        Type type;
        BigDecimal pricePerUnit;
        boolean active;


    }
}
