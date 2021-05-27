package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;
import pl.kamil.chefscookbook.user.domain.*;

import javax.persistence.*;
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentItem")
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
        recipe.setParentItem(this);
    }

    @ManyToOne(optional = false)
    private UserEntity userEntity;

   @ManyToMany(mappedBy = "items")
   private Set<Menu> menus = new HashSet<>();

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
