package pl.kamil.chefscookbook.food.application.port;

import pl.kamil.chefscookbook.food.domain.entity.Item;

public interface CreateItemUseCase {
    void createItem(Item item);
}
