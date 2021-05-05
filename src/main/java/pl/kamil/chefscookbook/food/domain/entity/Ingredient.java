package pl.kamil.chefscookbook.food.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

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

    public Ingredient(Item childItem, BigDecimal amount) {
        this.childItem = childItem;
        this.amount = amount;
    }
}
