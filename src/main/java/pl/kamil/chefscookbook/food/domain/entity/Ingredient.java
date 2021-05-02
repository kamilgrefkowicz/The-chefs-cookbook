package pl.kamil.chefscookbook.food.domain.entity;

import lombok.Data;
import pl.kamil.chefscookbook.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Ingredient extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    private BigDecimal amount;
}
