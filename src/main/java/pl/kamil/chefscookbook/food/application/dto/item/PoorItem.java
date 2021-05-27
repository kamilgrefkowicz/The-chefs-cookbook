package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.menu.application.dto.MenuDto;

import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.menu.application.dto.MenuDto.convertToPoorMenu;

@Value
@EqualsAndHashCode(callSuper = true)
public class PoorItem extends ItemDto {

    public PoorItem(Item item) {
        super(item.getId(),
                item.getName(),
                item.getUnit(),
                item.getType(),
                item.getUserEntity().getId());
    }

}
