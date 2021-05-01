package pl.kamil.chefscookbook.food.application.port;


import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.util.List;

public interface QueryItemsUseCase {
    List<Item> findAll();
}
