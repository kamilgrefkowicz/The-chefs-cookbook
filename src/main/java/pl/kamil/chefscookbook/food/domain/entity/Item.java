package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;
import pl.kamil.chefscookbook.user.domain.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"recipe"})
public class Item extends BaseEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentItem")
    private Recipe recipe;

    @ManyToOne(optional = false)
    private UserEntity userEntity;

   @ManyToMany(mappedBy = "items")
   private Set<Menu> menus = new HashSet<>();

    public Set<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }

    public void addIngredient(Item childItem, BigDecimal amount) {
        for (Ingredient ingredient : this.getIngredients()) {
            if (ingredient.getChildItem().equals(childItem)) {
                ingredient.setAmount(ingredient.getAmount().add(amount));
                return;
            }
        }
        this.getIngredients().add(new Ingredient(this.getRecipe(), childItem, amount));
    }

    //todo: move to service
    public Set<Item> getDependencies() {
        if (this.type.equals(BASIC)) return Collections.emptySet();
        Set<Item> dependencies = this.recipe.getIngredients()
                .stream()
                .map(Ingredient::getChildItem).collect(Collectors.toSet());

        dependencies
        .forEach(item -> dependencies.addAll(item.getDependencies()));

        return dependencies;
    }

    public Item(String name, Unit unit, Type type, UserEntity userEntity) {
        this.name = name;
        this.unit = unit;
        this.type = type;
        this.userEntity = userEntity;
        if (!type.equals(BASIC)) {
            this.recipe = new Recipe(this);
        }
    }
}
