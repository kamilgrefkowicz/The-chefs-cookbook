package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.EqualsAndHashCode;
import lombok.Value;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@Value
@EqualsAndHashCode(callSuper = true)
public class RichItem extends ItemDto{
    String description;
    Set<IngredientDto> ingredients;
    BigDecimal recipeYield;

    public RichItem (Item item) {
        super(item.getId(), item.getName(), item.getUnit(), item.getType(), item.getUserEntity().getId());
        this.description = item.getRecipe().getDescription();
        this.ingredients = item.getIngredients().stream()
        .map(IngredientDto::convertIngredientToDto)
        .collect(Collectors.toSet());
        this.recipeYield = item.getRecipe().getRecipeYield();
    }
}
