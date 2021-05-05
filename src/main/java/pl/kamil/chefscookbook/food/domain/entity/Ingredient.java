package pl.kamil.chefscookbook.food.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient extends BaseEntity {

    @ManyToOne
    Recipe recipe;

    @ManyToOne
    private Item childItem;

    private BigDecimal amount;

    public BigDecimal getRatio() {
        return this.amount.divide(this.getRecipe().getRecipeYield(), 4, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP);
    }
}
