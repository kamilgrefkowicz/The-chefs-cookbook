package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.dto.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Service
@RequiredArgsConstructor
public class QueryItemService implements QueryItemUseCase {

    private final ItemJpaRepository itemRepository;

    @Override
    @Transactional
    public List<PoorItem> findAll() {
        return itemRepository.findAll()
                .stream()
                .map(PoorItem::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RichItem findById(Long id) {
        return new RichItem(itemRepository.findById(id).orElseThrow());
    }

    @Override
    @Transactional
    public Map<ItemDto, BigDecimal> getMapOfAllDependencies(Long itemId, BigDecimal targetAmount) {
        Map<ItemDto, BigDecimal> dependencies = new LinkedHashMap<>();
        Item item = itemRepository.getOne(itemId);
        buildMapOfDependencies(item, targetAmount, dependencies);
        return dependencies;
    }



    private Map<ItemDto, BigDecimal> buildMapOfDependencies(Item item, BigDecimal targetAmount, Map<ItemDto, BigDecimal> map) {

        placeItemInMap(item, targetAmount, map);
        recursivelyGetDependencies(item, targetAmount, map);

        return map;

    }
    private void placeItemInMap(Item item, BigDecimal targetAmount, Map<ItemDto, BigDecimal> map) {
        targetAmount = targetAmount.setScale(3, RoundingMode.HALF_EVEN);
        ItemDto toPlace = convertToDto(item);
        if (map.containsKey(toPlace)) map.put(toPlace, map.get(toPlace).add(targetAmount));
        else map.put(toPlace, targetAmount);
    }

    private void recursivelyGetDependencies(Item item, BigDecimal targetAmount, Map<ItemDto, BigDecimal> map) {
        if (!item.getType().equals(BASIC())) {
            for (Ingredient ingredient : item.getIngredients()) {
                BigDecimal amountForNext = targetAmount.multiply(ingredient.getRatio());
                buildMapOfDependencies(ingredient.getChildItem(), amountForNext, map);
            }
        }
    }



}
