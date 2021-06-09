package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class ItemDto {

    private Long id;
    private String name;
    private Unit unit;
    private Type type;
    private Long userEntityId;

    public static ItemDto convertToDto(Item item) {
        if (item.getType().equals(BASIC)) return new PoorItem(item);
        return new RichItem(item);
    }

//    public static PoorItem convertToPoorItem(Item item) {
//        return new PoorItem(item);
//    }

}
