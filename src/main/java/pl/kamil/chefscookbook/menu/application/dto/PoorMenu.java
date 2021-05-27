package pl.kamil.chefscookbook.menu.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.menu.domain.Menu;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToPoorItem;

@Value
@EqualsAndHashCode(callSuper = true)
public class PoorMenu extends MenuDto{

     Set<PoorItem> items;

     public PoorMenu(Menu menu) {
          super(menu.getId(), menu.getName(), menu.getLocalDate());
          this.items = menu.getItems().stream()
                  .map(ItemDto::convertToPoorItem)
                  .collect(Collectors.toSet());

     }


}
