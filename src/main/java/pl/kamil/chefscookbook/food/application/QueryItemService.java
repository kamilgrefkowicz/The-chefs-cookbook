package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueryItemService implements QueryItemUseCase {

    private final ItemJpaRepository itemRepository;

    @Override
    public List<PoorItem> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(this::toPoorItem)
                .collect(Collectors.toList());
    }

    private PoorItem toPoorItem(Item item) {
        return PoorItem.builder()
                .id(item.getId())
                .name(item.getName())
                .unit(item.getUnit())
                .type(item.getType())
                .pricePerUnit(item.getPricePerUnit())
                .active(item.isActive())
                .build();
    }
}
