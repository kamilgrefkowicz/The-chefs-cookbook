package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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

    @OneToMany(mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne
    @MapsId
    private Item item;

    private BigDecimal defaultYield;


}
