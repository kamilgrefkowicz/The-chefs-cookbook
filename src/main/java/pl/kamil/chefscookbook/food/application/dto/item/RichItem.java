package pl.kamil.chefscookbook.food.application.dto.item;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.food.application.dto.ingredient.IngredientDto;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RichItem extends ItemDto{
    private String description;
    private Set<IngredientDto> ingredients = new LinkedHashSet<>();
    private BigDecimal recipeYield;

    public RichItem (Item item) {
        super(item.getId(), item.getName(), item.getUnit(), item.getType(), item.getUserEntity().getId());
        this.description = item.getRecipe().getDescription();
        item.getIngredients().stream()
        .forEach(ingredient -> ingredients.add(new IngredientDto(ingredient)));
        this.recipeYield = item.getRecipe().getRecipeYield();
    }
}
