package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient extends BaseEntity {

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Recipe recipe;

    @ManyToOne
    private Item childItem;

    private BigDecimal amount;

    public BigDecimal getRatio() {
        return this.amount.divide(this.getRecipe().getRecipeYield(), 4, RoundingMode.HALF_UP);
    }

    public Item getParentItem() {
        return this.recipe.getParentItem();
    }

    public void removeSelf() {
        getParentItem().getIngredients().remove(this);
    }
}
