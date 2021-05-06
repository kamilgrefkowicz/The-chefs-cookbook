package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"recipe"})
public class Item extends BaseEntity {

    private String name;

    @ManyToOne
    private Unit unit;

    @ManyToOne
    private Type type;

    private BigDecimal pricePerUnit;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentItem")
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.setParentItem(this);
    }

    public Set<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }


    public Set<Item> getDependencies() {
        if (this.type.equals(Type.BASIC())) return Collections.emptySet();
        Set<Item> dependencies = this.recipe.getIngredients()
                .stream()
                .map(Ingredient::getChildItem).collect(Collectors.toSet());

        dependencies.stream()
        .filter(item -> !item.getType().equals(Type.BASIC()))
        .forEach(item -> dependencies.addAll(item.getDependencies()));

        return dependencies;
    }

}
