package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.PoorItem.toPoorItem;
import static pl.kamil.chefscookbook.food.application.port.QueryItemUseCase.RichItem.*;
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
                .map(PoorItem::toPoorItem)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RichItem findById(Long id) {
        return toRichItem(itemRepository.findById(id).orElseThrow());
    }

    @Override
    @Transactional
    public FullItem getFullItem(GetFullItemCommand command) {
        Item item = itemRepository.findById(command.getItemId()).orElseThrow();
        if (!item.isActive()) throw new IllegalArgumentException();

        Map<PoorItem, BigDecimal> map = new LinkedHashMap<>();
        createMapOfAllDependenciesWithAmounts(item, command.getTargetAmount(), map);

        return FullItem.builder()
                .id(item.getId())
                .name(item.getName())
                .unit(item.getUnit())
                .type(item.getType())
                .pricePerUnit(item.getPricePerUnit())
                .active(item.isActive())
                .recipe(item.getRecipe())
                .dependencyMapWithAmounts(map)
                .build();


    }

    private Map<PoorItem, BigDecimal> createMapOfAllDependenciesWithAmounts(Item item, BigDecimal targetAmount, Map<PoorItem, BigDecimal> map) {

        placeItemInMap(item, targetAmount, map);
        recursivelyGetDependencies(item, targetAmount, map);

        return map;

    }

    private void placeItemInMap(Item item, BigDecimal targetAmount, Map<PoorItem, BigDecimal> map) {
        PoorItem toPlace = toPoorItem(item);
        if (map.containsKey(toPlace)) map.put(toPlace, map.get(toPlace).add(targetAmount));
        else map.put(toPlace, targetAmount);
    }

    private void recursivelyGetDependencies(Item item, BigDecimal targetAmount, Map<PoorItem, BigDecimal> map) {
        if (!item.getType().equals(BASIC())) {
            for (Ingredient ingredient : item.getIngredients()) {
                BigDecimal amountForNext = targetAmount.multiply(ingredient.getRatio());
                createMapOfAllDependenciesWithAmounts(ingredient.getChildItem(), amountForNext, map);
            }
        }
    }



}
