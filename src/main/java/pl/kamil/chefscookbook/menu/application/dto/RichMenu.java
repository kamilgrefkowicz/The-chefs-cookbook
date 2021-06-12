package pl.kamil.chefscookbook.menu.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.menu.domain.Menu;

import java.util.Set;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode(callSuper = true)
public class RichMenu extends MenuDto{

    Set<PoorItem> items;

    public RichMenu(Menu menu) {
        super(menu.getId(), menu.getName());
        this.items = menu.getItems().stream()
                .map(PoorItem::new)
                .collect(Collectors.toSet());
    }
}
