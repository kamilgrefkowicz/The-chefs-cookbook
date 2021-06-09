package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.QueryItemService;
import pl.kamil.chefscookbook.food.database.IngredientRepository;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToDto;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.NOT_AUTHORIZED;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class QueryItem implements QueryItemService {

    private final ItemRepository itemRepository;
    private final UserSecurityService userSecurity;
    private final IngredientRepository ingredientRepository;

    @Override
    public List<PoorItem> findAllItemsBelongingToUser(Principal user) {

        return itemRepository.findAllByUserEntityId(getUserId(user))
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

    @Override
    public List<PoorItem> findAllItemsAffectedByDelete(Long itemId) {
        return ingredientRepository.findAllByChildItemId(itemId).stream()
                .map(ingredient -> new PoorItem(ingredient.getParentItem()))
                .collect(Collectors.toList());

    }

    @Override
    public List<PoorItem> findAllEligibleDishesForMenu(Principal user, Long menuId) {

        return itemRepository.findAllDishesByUser(getUserId(user)).stream()
                .filter(item -> alreadyInTheMenu(menuId, item))
                .map(ItemDto::convertToPoorItem)
                .collect(Collectors.toList());
    }

    private Long getUserId(Principal user) {
        return Long.valueOf(user.getName());
    }

    private boolean alreadyInTheMenu(Long menuId, Item item) {
        return !item.getMenus().stream().map(BaseEntity::getId).collect(Collectors.toSet()).contains(menuId);
    }


    private ItemAutocompleteDto convertToAutocompleteDto(Item item) {
        return new ItemAutocompleteDto(item.getId(), item.getName() + " (" + item.getUnit().toString() + ")");
    }



    @Override
    @Transactional
    public Response<RichItem> findById(Long itemId, Principal user) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty()) return Response.failure(NOT_FOUND);
        RichItem item = new RichItem(optionalItem.get());
        if (!userSecurity.isOwner(item.getUserEntityId(), user)) return Response.failure(NOT_AUTHORIZED);
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
        if (!item.getType().equals(BASIC) && !targetAmount.equals(BigDecimal.ZERO)) {
            for (Ingredient ingredient : item.getIngredients()) {
                BigDecimal amountForNext = targetAmount.multiply(ingredient.getRatio());
                buildMapOfDependencies(ingredient.getChildItem(), amountForNext, map);
            }
        }
    }


}