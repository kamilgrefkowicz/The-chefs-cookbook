package pl.kamil.chefscookbook.food.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe extends BaseEntity {

    private String description = "";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ingredient> ingredients = new LinkedHashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Item parentItem;

    private BigDecimal recipeYield = BigDecimal.ONE;


    public Recipe(Item parentItem) {
        this.parentItem = parentItem;
    }
}
