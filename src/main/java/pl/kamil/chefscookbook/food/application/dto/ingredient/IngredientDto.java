package pl.kamil.chefscookbook.food.application.dto.ingredient;

import lombok.Data;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;

import java.math.BigDecimal;


@Data
public class IngredientDto {

    private Long ingredientId;

    private PoorItem childItem;

    private BigDecimal amount;

    private BigDecimal ratio;

    public IngredientDto(Ingredient ingredient) {
        this.ingredientId = ingredient.getId();
        this.childItem = new PoorItem(ingredient.getChildItem());
        this.amount = ingredient.getAmount();
        this.ratio = ingredient.getRatio();
    }

}
