package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ModifyItemService implements ModifyItemUseCase {

    private final ItemJpaRepository itemRepository;

    @Override
    @Transactional
    public void createItem(CreateNewItemCommand command) {
        itemRepository.save(command.toItem());
    }
}
