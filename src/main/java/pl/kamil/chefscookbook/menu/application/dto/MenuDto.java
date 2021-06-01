package pl.kamil.chefscookbook.menu.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kamil.chefscookbook.menu.domain.Menu;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public abstract class MenuDto {

    private Long menuId;
    private String menuName;



}
