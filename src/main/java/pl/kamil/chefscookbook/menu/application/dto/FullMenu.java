package pl.kamil.chefscookbook.menu.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.menu.domain.Menu;

import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class FullMenu extends MenuDto{
    Set<RichItem> dishes;
    Set<RichItem> intermediates;
    Set<PoorItem> basics;

    public FullMenu(Menu menu, Set<RichItem> dishes, Set<RichItem> intermediates, Set<PoorItem> basics) {
        super(menu.getId(), menu.getName());
        this.dishes = dishes;
        this.intermediates = intermediates;
        this.basics = basics;
    }
}
