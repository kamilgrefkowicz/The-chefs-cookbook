package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "recipe")
public class Item extends BaseEntity {

    private String name;

    @ManyToOne
    private Unit unit;

    @ManyToOne(optional = false)
    private Type type;

    private BigDecimal pricePerUnit;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "item")
    private Recipe recipe;

    boolean active = false;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.setItem(this);
    }
}
