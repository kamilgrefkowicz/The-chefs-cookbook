package pl.kamil.chefscookbook.food.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public abstract class ItemDto {

    private Long id;
    private String name;
    private Unit unit;
    private Type type;

    public static ItemDto convertToDto(Item item) {
        if (item.getType().equals(Type.BASIC())) return new PoorItem(item);
        return new RichItem(item);
    }

}
