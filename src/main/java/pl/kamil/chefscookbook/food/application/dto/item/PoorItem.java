package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;

@Value
@EqualsAndHashCode(callSuper = true)
public class PoorItem extends ItemDto {

    public PoorItem(Item item) {
        super(item.getId(), item.getName(), item.getUnit(), item.getType(), item.getUserEntity().getId());
    }

}
