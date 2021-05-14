package pl.kamil.chefscookbook.food.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.application.dto.item.RichItem;
import pl.kamil.chefscookbook.food.application.port.ModifyItemUseCase;
import pl.kamil.chefscookbook.food.database.IngredientJpaRepository;
import pl.kamil.chefscookbook.food.database.ItemJpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.entity.Recipe;

import javax.transaction.Transactional;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Service
@RequiredArgsConstructor
public class ModifyItemService implements ModifyItemUseCase {

    private final ItemJpaRepository itemRepository;
    private final IngredientJpaRepository ingredientRepository;

    @Override
    @Transactional
    public PoorItem createItem(CreateNewItemCommand command) {
        Item item = command.toItem();
        generateRecipeIfApplicable(item);
        return new PoorItem(itemRepository.save(item));
    }


    @Transactional
    @Override
    public RichItem addIngredientToRecipe(AddIngredientCommand command) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Item childItem = itemRepository.getOne(command.getChildItemId());
        //todo implement exceptions
        checkForLoops(parentItem, childItem);
        addIngredient(parentItem, childItem, command.getAmount());

        return new RichItem(itemRepository.save(parentItem));
    }


    @Override
    @Transactional
    public RichItem setYield(SetYieldCommand command) {
        Item item = itemRepository.findById(command.getParentItemId()).orElseThrow();
        item.getRecipe().setRecipeYield(command.getItemYield());
        return new RichItem(itemRepository.save(item));
    }

    @Override
    @Transactional
    public RichItem updateDescription(UpdateDescriptionCommand command) {
        Item item = itemRepository.getOne(command.getParentItemId());
        item.getRecipe().setDescription(command.getDescription());
        return new RichItem(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(DeleteItemCommand command) {

        removeThisItemFromAllDependencies(command.getItemId());

        itemRepository.deleteById(command.getItemId());

    }

    private void removeThisItemFromAllDependencies(Long itemId) {

        ingredientRepository.findAllByChildItemId(itemId)
                .forEach(Ingredient::removeSelf);
    }

    @Override
    @Transactional
    public RichItem removeIngredientFromRecipe(RemoveIngredientFromRecipeCommand command) {
        Item parentItem = itemRepository.getOne(command.getParentItemId());
        Ingredient ingredientToRemove = ingredientRepository.getOne(command.getIngredientId());

        ingredientToRemove.removeSelf();

        return new RichItem(itemRepository.save(parentItem));
    }




    private void addIngredient(Item parentItem, Item childItem, BigDecimal amount) {
        for (Ingredient ingredient : parentItem.getIngredients()) {
            if (ingredient.getChildItem().equals(childItem)) {
                ingredient.setAmount(ingredient.getAmount().add(amount));
                return;
            }
        }
        parentItem.getIngredients().add(new Ingredient(parentItem.getRecipe(), childItem, amount));
    }

    private void checkForLoops(Item parentItem, Item childItem) {
        if ((childItem.getDependencies().contains(parentItem) || childItem.equals(parentItem)))
            throw new IllegalArgumentException();
    }


    private void generateRecipeIfApplicable(Item item) {
        if (!item.getType().equals(BASIC())) item.setRecipe(new Recipe());
    }
}
