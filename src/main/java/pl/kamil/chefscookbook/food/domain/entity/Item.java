package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.menu.domain.Menu;
import pl.kamil.chefscookbook.shared.jpa.OwnedEntity;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static pl.kamil.chefscookbook.food.domain.staticData.Type.BASIC;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"recipe"})
public class Item extends OwnedEntity {

    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToOne(mappedBy = "parentItem", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private Recipe recipe;


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


    public Item(String name, Unit unit, Type type, UserEntity userEntity) {
        this.name = name;
        this.unit = passedOrDefaultToKg(unit);
        this.type = type;
        this.userEntity = userEntity;
        if (!type.equals(BASIC)) {
            this.recipe = new Recipe(this);
        }
    }

    private Unit passedOrDefaultToKg(Unit unit) {
        if (unit == null) return Unit.KILOGRAM;
        return unit;
    }
}
