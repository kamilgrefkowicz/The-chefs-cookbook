package pl.kamil.chefscookbook.food.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Recipe  {

    @Id
    private Long id;

    @Version
    private Long version;

    private String uuid = UUID.randomUUID().toString();

    @Column(length = 1000)
    private String description = "";

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe", fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Ingredient> ingredients = new LinkedHashSet<>();

    @OneToOne
    @MapsId
    private Item parentItem;

    private BigDecimal recipeYield = BigDecimal.ONE;


    public Recipe(Item parentItem) {
        this.parentItem = parentItem;
    }
}
