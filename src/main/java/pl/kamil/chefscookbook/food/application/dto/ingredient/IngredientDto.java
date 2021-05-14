package pl.kamil.chefscookbook.food.application.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.kamil.chefscookbook.food.application.dto.item.ItemDto;
import pl.kamil.chefscookbook.food.application.dto.item.PoorItem;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.math.BigDecimal;

import static pl.kamil.chefscookbook.food.application.dto.item.ItemDto.convertToPoorItem;

@Data
public class IngredientDto {

    private Long ingredientId;

    private PoorItem childItem;

    private BigDecimal amount;

    private BigDecimal ratio;

    public IngredientDto(Ingredient ingredient) {
        this.ingredientId = ingredient.getId();
        this.childItem = convertToPoorItem(ingredient.getChildItem());
        this.amount = ingredient.getAmount();
        this.ratio = ingredient.getRatio();
    }

    public static IngredientDto convertIngredientToDto(Ingredient ingredient) {
        return new IngredientDto(ingredient);
    }
}
