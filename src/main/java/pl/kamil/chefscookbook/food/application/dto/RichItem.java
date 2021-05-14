package pl.kamil.chefscookbook.food.application.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.math.BigDecimal;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = true)
public class RichItem extends ItemDto{
    String description;
    Set<Ingredient> ingredients;
    BigDecimal recipeYield;

    public RichItem (Item item) {
        super(item.getId(), item.getName(), item.getUnit(), item.getType());
        this.description = item.getRecipe().getDescription();
        this.ingredients = item.getIngredients();
        this.recipeYield = item.getRecipe().getRecipeYield();
    }
}
