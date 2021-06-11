package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;

import java.util.UUID;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Data
@AllArgsConstructor
@EqualsAndHashCode(of = "uuid")
@NoArgsConstructor
public abstract class ItemDto {

    private String uuid = UUID.randomUUID().toString();
    private Long id;
    private String name;
    private Unit unit;
    private Type type;
    private Long userEntityId;

    public static ItemDto convertToDto(Item item) {
        if (item.getType().equals(BASIC)) return new PoorItem(item);
        return new RichItem(item);
    }

    protected ItemDto(Long id, String name, Unit unit, Type type, Long userEntityId) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.type = type;
        this.userEntityId = userEntityId;
    }
}
