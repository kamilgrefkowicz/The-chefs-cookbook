package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;

@Value
public class ItemAutocompleteDto {

    Long value;
    String label;

    public ItemAutocompleteDto(Item item) {
        this.value = item.getId();
        this.label = item.getName() + " (" + item.getUnit() + ")";
    }

}
