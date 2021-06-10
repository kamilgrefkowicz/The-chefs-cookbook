package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Value;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RichItem extends ItemDto{
    private String description;
    private Set<IngredientDto> ingredients;
    private BigDecimal recipeYield;

    public RichItem (Item item) {
        super(item.getId(), item.getName(), item.getUnit(), item.getType(), item.getUserEntity().getId());
        this.description = item.getRecipe().getDescription();
        this.ingredients = item.getIngredients().stream()
        .map(IngredientDto::new)
        .collect(Collectors.toSet());
        this.recipeYield = item.getRecipe().getRecipeYield();
    }
}
