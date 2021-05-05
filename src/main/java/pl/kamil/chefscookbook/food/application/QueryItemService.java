package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.PoorItem.toPoorItem;

@Service
@RequiredArgsConstructor
public class QueryItemService implements QueryItemUseCase {

    private final ItemJpaRepository itemRepository;

    @Override
    @Transactional
    public List<PoorItem> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(PoorItem::toPoorItem)
                .collect(Collectors.toList());
    }





}
