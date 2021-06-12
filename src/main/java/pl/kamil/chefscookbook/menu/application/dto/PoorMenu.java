package pl.kamil.chefscookbook.menu.application.dto;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.menu.domain.Menu;

@Value
@EqualsAndHashCode(callSuper = true)
public class PoorMenu extends MenuDto {


    public PoorMenu(Menu menu) {
        super(menu.getId(), menu.getName());
    }


}
