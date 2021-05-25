package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.UserSecurityService;
import pl.kamil.chefscookbook.user.application.UserService;
import pl.kamil.chefscookbook.user.application.port.UserSecurityUseCase;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Service
@RequiredArgsConstructor
public class QueryItemService implements QueryItemUseCase {

    private final ItemJpaRepository itemRepository;
    private final UserSecurityUseCase userSecurity;

    @Override
    public List<PoorItem> findAllItemsBelongingToUser(Principal user) {

        return itemRepository.findAllByUserEntityId(Long.valueOf(user.getName()))
                .stream()
                .map(PoorItem::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemAutocompleteDto> findForAutocomplete(String term, Long userId) {
        return itemRepository.findForAutocomplete(term, userId)
                .stream()
                .map(this::convertToAutocompleteDto)
                .collect(Collectors.toList());

    }
    private ItemAutocompleteDto convertToAutocompleteDto(Item item) {
        return new ItemAutocompleteDto(item.getId(), item.getName() + " (" + item.getUnit().toString() + ")");
    }



    @Override
    @Transactional
    public Response<RichItem> findById(Long itemId, Principal user) {
        RichItem item = new RichItem(itemRepository.getOne(itemId));
        if (!userSecurity.isOwner(item.getUserEntityId(), user)) return Response.failure("You're not authorized to view this item");
        return Response.success(item);
    }

    @Override
    @Transactional
    public Map<ItemDto, BigDecimal> getMapOfAllDependencies(QueryItemWithDependenciesCommand command) {
        Map<ItemDto, BigDecimal> dependencies = new LinkedHashMap<>();
        Item item = itemRepository.getOne(command.getItemId());
        buildMapOfDependencies(item, command.getTargetAmount(), dependencies);
        return dependencies;
    }


    private void buildMapOfDependencies(Item item, BigDecimal targetAmount, Map<ItemDto, BigDecimal> map) {
        placeItemInMap(item, targetAmount, map);
        recursivelyGetDependencies(item, targetAmount, map);
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
