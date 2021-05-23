package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.Data;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Item;

@Value
public class ItemAutocompleteDto {

    long value;
    String label;


}
