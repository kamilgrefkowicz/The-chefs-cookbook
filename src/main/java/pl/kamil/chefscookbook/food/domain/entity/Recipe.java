package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe extends BaseEntity {

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", fetch = FetchType.EAGER)
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Item parentItem;

    private BigDecimal recipeYield;

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        ingredients.forEach(ingredient -> ingredient.setRecipe(this));
    }
}
