package pl.kamil.chefscookbook.food.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemService.*;
import pl.kamil.chefscookbook.food.database.IngredientRepository;
import pl.kamil.chefscookbook.food.database.ItemRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.UserSecurityService;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static pl.kamil.chefscookbook.food.domain.staticData.Type.*;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.*;

@ExtendWith(MockitoExtension.class)
class ModifyItemTest {

    @Mock
    ItemRepository itemRepository;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    UserSecurityService userSecurity;

    @InjectMocks
    ModifyItem modifyItem;

    @Captor
    ArgumentCaptor<Item> itemCaptor;

    @Test
    void creatingItemShouldCallRepositoryToCheckIfNamePresent() {
        Principal user = getPrincipal(1L);
        Item alreadyExists = getBasicItem("test item");
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.of(alreadyExists));

        modifyItem.createItem(new CreateNewItemCommand("test", BASIC, Unit.KILOGRAM), user);

        verify(itemRepository).findFirstByNameAndUserEntityId("test", 1L);
    }

    @Test
    void creatingItemWhenSameItemNameByUserExistsShouldReturnFailure() {
        Principal user = getPrincipal(1L);
        Item alreadyExists = getBasicItem("test item");
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.of(alreadyExists));

        Response<ItemDto> created = modifyItem.createItem(new CreateNewItemCommand("test", BASIC, Unit.KILOGRAM), user);

        assertFalse(created.isSuccess());
        assertThat(created.getError(), equalTo(ITEM_NAME_TAKEN));
        verify(itemRepository, times(0)).save(any());

    }

    @Test
    void creatingValidItemShouldCallRepositoryToSave() {
        Principal user = getPrincipal(5L);
        UserEntity userEntity = new UserEntity();
        userEntity.setId(5L);
        Item returned = getBasicItem("returned test");
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(userEntity);
        when(itemRepository.save(any())).thenReturn(returned);

        modifyItem.createItem(new CreateNewItemCommand("test", BASIC, Unit.KILOGRAM), user);

        verify(itemRepository).save(itemCaptor.capture());
        Item saved = itemCaptor.getValue();
        assertThat(saved.getName(), equalTo("test"));
        assertThat(saved.getType(), equalTo(BASIC));
        assertThat(saved.getUnit(), equalTo(Unit.KILOGRAM));
        assertThat(saved.getUserEntity().getId(), equalTo(5L));
    }

    @Test
    void creatingValidItemShouldReturnSuccessfulResponse() {
        Principal principal = getPrincipal(5L);
        UserEntity userEntity = getUserEntity(5L);
        Item returned = getBasicItem("returned test");
        returned.setUserEntity(userEntity);
        when(itemRepository.findFirstByNameAndUserEntityId(any(), any())).thenReturn(Optional.empty());
        when(userRepository.getOne(any())).thenReturn(userEntity);
        when(itemRepository.save(any())).thenReturn(returned);

        Response<ItemDto> created = modifyItem.createItem(new CreateNewItemCommand("test", BASIC, Unit.KILOGRAM), principal);

        assertTrue(created.isSuccess());
        ItemDto dto = created.getData();
        assertThat(dto.getName(), equalTo("returned test"));
        assertThat(dto.getType(), equalTo(BASIC));
        assertThat(dto.getUnit(), equalTo(Unit.KILOGRAM));
        assertThat(dto.getUserEntityId(), equalTo(5L));
    }

    @Test
    void addingIngredientShouldCallRepositoryToRetrieveItems() {
        Principal principal = getPrincipal(1L);

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 2L, BigDecimal.ONE), principal);

        verify(itemRepository, times(2)).getOne(any());
    }

    @Test
    void addingIngredientShouldCallUserSecurityToValidateEligibility() {
        Principal principal = getPrincipal(3L);
        Item parentItem = getItem(1L);
        Item childItem = getItem(2L);
        when(itemRepository.getOne(1L)).thenReturn(parentItem);
        when(itemRepository.getOne(2L)).thenReturn(childItem);
        when(userSecurity.belongsTo(any(), any())).thenReturn(true);

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 2L, BigDecimal.ONE), principal);

        verify(userSecurity).belongsTo(parentItem, principal);
        verify(userSecurity).belongsToOrIsPublic(childItem, principal);

    }

    @Test
    void addingNonEligibleIngredientShouldReturnFailure() {
        Principal principal = getPrincipal(1L);
        when(userSecurity.belongsTo(any(), any())).thenReturn(false);

        Response<ItemDto> modification = modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 2L, BigDecimal.ONE), principal);

        assertFalse(modification.isSuccess());
        assertThat(modification.getError(), equalTo(NOT_AUTHORIZED));
        verify(itemRepository, times(0)).save(any());

    }
    @Test
    void attemptingToMakeAnItemItsOwnIngredientShouldResultInShortLoopError() {
        Principal principal = getPrincipal(3L);
        Item parentItem = getBasicItem("test");
        when(itemRepository.getOne(any())).thenReturn(parentItem);
        passSecurity(true);

        Response<ItemDto> modification =modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 1L, BigDecimal.ONE), principal);

        assertFalse(modification.isSuccess());
        assertThat(modification.getError(), equalTo(LOOP_SHORT));
        verify(itemRepository, times(0)).save(any());
    }
    @Test
    void attemptingToAddIngredientAlreadyDependingOnParentShouldResultInLongLoopError() {
        Principal principal = getPrincipal(3L);
        Item childItem = getItem(1L, INTERMEDIATE);
        Item parentItem = getItem(2L, INTERMEDIATE);
        childItem.addIngredient(parentItem, BigDecimal.ONE);
        when(itemRepository.getOne(1L)).thenReturn(parentItem);
        when(itemRepository.getOne(2L)).thenReturn(childItem);
        passSecurity(true);

        Response<ItemDto> modification =modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 2L, BigDecimal.ONE), principal);

        assertFalse(modification.isSuccess());
        assertThat(modification.getError(), containsString(LOOP_LONG));
        verify(itemRepository, times(0)).save(any());
    }
    @Test
    void addingValidIngredientShouldCallRepositoryToSave() {
        Principal principal = getPrincipal(3L);
        Item childItem = getItem(1L, INTERMEDIATE);
        childItem.setName("child");
        Item parentItem = getItem(2L, INTERMEDIATE);
        parentItem.setName("parent");
        parentItem.setUserEntity(getUserEntity(1L));
        when(itemRepository.getOne(1L)).thenReturn(parentItem);
        when(itemRepository.getOne(2L)).thenReturn(childItem);
        passSecurity(true);
        when(itemRepository.save(any())).thenReturn(getItem(1L, INTERMEDIATE));

        modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 2L, BigDecimal.ONE), principal);

        verify(itemRepository).save(itemCaptor.capture());
        Item saved = itemCaptor.getValue();
        assertThat(saved.getName(), equalTo("parent"));
        assertThat(saved.getIngredients(), hasSize(1));
        assertThat(saved.getIngredients().stream().findFirst().get().getChildItem().getName(), equalTo("child"));
    }
    @Test
    void addingValidIngredientShouldReturnSuccessfulResponse() {
        Principal principal = getPrincipal(3L);
        Item childItem = getItem(1L, INTERMEDIATE);
        Item parentItem = getItem(2L, INTERMEDIATE);
        parentItem.setUserEntity(getUserEntity(1L));
        when(itemRepository.getOne(1L)).thenReturn(parentItem);
        when(itemRepository.getOne(2L)).thenReturn(childItem);
        passSecurity(true);
        Item returned = getItem(2L, INTERMEDIATE);
        returned.addIngredient(childItem, BigDecimal.ONE);
        returned.setUserEntity(getUserEntity(1L));
        when(itemRepository.save(any())).thenReturn(returned);

        Response<ItemDto> response = modifyItem.addIngredientToRecipe(new AddIngredientCommand(1L, 2L, BigDecimal.ONE), principal);
        RichItem modified = (RichItem) response.getData();

        assertTrue(response.isSuccess());
        assertThat(modified.getId(), equalTo(2L));
        assertThat(modified.getIngredients(), hasSize(1));
        assertThat(modified.getUserEntityId(), equalTo(1L));
    }
    //repeated security logic is not tested
    @Test
    void setYieldCommandShouldCallRepositoryToSave() {
        Principal principal = getPrincipal(1L);
        Optional<Item> optionalItem = Optional.of(getItem(1L, DISH));
        when(itemRepository.findById(any())).thenReturn(optionalItem);
        passSecurity();
        when(itemRepository.save(any())).thenReturn(getItem(1L, DISH));

        modifyItem.setYield(new SetYieldCommand(1L, new BigDecimal(2)), principal);

        verify(itemRepository).save(itemCaptor.capture());
        Item saved = itemCaptor.getValue();
        assertThat(saved.getRecipe().getRecipeYield(), equalTo(new BigDecimal(2)));
    }
    @Test
    void updateDescriptionShouldCallRepositoryToSave(){
        Principal principal = getPrincipal(1L);
        Optional<Item> optionalItem = Optional.of(getItem(1L, DISH));
        when(itemRepository.findById(any())).thenReturn(optionalItem);
        passSecurity();
        when(itemRepository.save(any())).thenReturn(getItem(1L, DISH));

        modifyItem.updateDescription(new UpdateDescriptionCommand(1L, "test description"), principal);

        verify(itemRepository).save(itemCaptor.capture());
        Item saved = itemCaptor.getValue();
        assertThat(saved.getRecipe().getDescription(), equalTo("test description"));
    }
    @Test
    void deleteItemShouldCallRepositoryToFindAllItsDependants() {
        Principal principal = getPrincipal(1L);
        Optional<Item> optionalItem = Optional.of(getItem(3L, DISH));
        when(itemRepository.findById(any())).thenReturn(optionalItem);
        passSecurity();

        modifyItem.deleteItem(new DeleteItemCommand(3L), principal);

        verify(ingredientRepository).findAllByChildItemId(3L);
    }
    @Test
    void deletingItemShouldCallRemoveSelfMethodOnIngredientsItIsChildItemOf() {
        Ingredient ingredient = Mockito.mock(Ingredient.class);
        Principal principal = getPrincipal(1L);
        Optional<Item> optionalItem = Optional.of(getItem(3L, DISH));
        when(itemRepository.findById(any())).thenReturn(optionalItem);
        passSecurity();
        when(ingredientRepository.findAllByChildItemId(any())).thenReturn(Collections.singletonList(ingredient));

        modifyItem.deleteItem(new DeleteItemCommand(3L), principal);

        verify(ingredient).removeSelf();
    }
    @Test
    void removingIngredientFromRecipeShouldCallRepositoryToSave() {
        Principal principal = getPrincipal(1L);
        Item parentItem = getItem(3L, DISH);
        Item childItem = getItem(4L, BASIC);
        parentItem.addIngredient(childItem, BigDecimal.ONE);
        Ingredient ingredient = parentItem.getIngredients().stream().findFirst().get();
        when(itemRepository.getOne(any())).thenReturn(parentItem);
        when(ingredientRepository.getOne(any())).thenReturn(ingredient);
        passSecurity(true);
        when(itemRepository.save(any())).thenReturn(getItem(1L, DISH));

        modifyItem.removeIngredientFromRecipe(new RemoveIngredientFromRecipeCommand(3L, 2L), principal);

        verify(itemRepository).save(itemCaptor.capture());
        Item item = itemCaptor.getValue();
        assertThat(item.getIngredients(), hasSize(0));
    }

    private void passSecurity() {
        when(userSecurity.belongsTo(any(), any())).thenReturn(true);
    }

    private void passSecurity(boolean all) {
        passSecurity();
        when(userSecurity.belongsToOrIsPublic(any(), any())).thenReturn(true);
    }

    private Item getItem(long id, Type type) {
        Item item = new Item("", Unit.KILOGRAM, type, new UserEntity());
        item.setId(id);
        return item;
    }

    private Item getItem(long id) {
        Item item = new Item();
        item.setId(id);
        return item;
    }


    private UserEntity getUserEntity(long id) {
        UserEntity user = new UserEntity();
        user.setId(id);
        return user;
    }

    private Principal getPrincipal(long id) {
        return new Principal() {
            @Override
            public String getName() {
                return String.valueOf(id);
            }
        };
    }

    private Item getBasicItem(String name) {
        return new Item(name, Unit.KILOGRAM, BASIC, new UserEntity());

    }

}