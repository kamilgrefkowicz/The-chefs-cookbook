package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.QueryItemsUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QueryItemsUseCaseService implements QueryItemsUseCase {

    private final ItemJpaRepository itemRepository;

    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }
}
