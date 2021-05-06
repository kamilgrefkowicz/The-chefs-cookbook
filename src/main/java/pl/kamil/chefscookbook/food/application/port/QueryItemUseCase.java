package pl.kamil.chefscookbook.food.application.port;


import lombok.Builder;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.entity.Recipe;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface QueryItemUseCase {
    List<PoorItem> findAll();

    RichItem findById(Long id);

    FullItem getFullItem(GetFullItemCommand command);


    @Value
    @Builder
    class PoorItem {
        Long id;
        String name;
        Unit unit;
        Type type;
        BigDecimal pricePerUnit;
        boolean active;

        public static PoorItem toPoorItem(Item item) {
            return PoorItem.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .unit(item.getUnit())
                    .type(item.getType())
                    .pricePerUnit(item.getPricePerUnit())
                    .active(item.isActive())
                    .build();
        }
    }

    @Value
    @Builder
    class RichItem {

        Long id;
        String name;
        Unit unit;
        Type type;
        BigDecimal pricePerUnit;
        boolean active;
        Recipe recipe;

        public static RichItem toRichItem(Item item) {
             return RichItem.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .unit(item.getUnit())
                    .type(item.getType())
                    .pricePerUnit(item.getPricePerUnit())
                    .active(item.isActive())
                    .recipe(item.getRecipe())
                    .build();
        }

    }
    @Value
    class GetFullItemCommand {
        Long itemId;
        BigDecimal targetAmount;

        public GetFullItemCommand(Long itemId) {
            this.itemId = itemId;
            targetAmount = BigDecimal.valueOf(1);
        }

        public GetFullItemCommand(Long itemId, BigDecimal targetAmount) {
            this.itemId = itemId;
            this.targetAmount = targetAmount;
        }
    }

    @Value
    @Builder
    class FullItem {
        Long id;
        String name;
        Unit unit;
        Type type;
        BigDecimal pricePerUnit;
        boolean active;
        Recipe recipe;
        Map<PoorItem, BigDecimal> dependencyMapWithAmounts;
    }

}











