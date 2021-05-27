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

    private String description = "";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    private Item parentItem;

    private BigDecimal recipeYield;


    public Recipe(BigDecimal recipeYield) {
        this.recipeYield = recipeYield;
    }
}
