package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.kamil.chefscookbook.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recipe extends BaseEntity {

    private String description;

//    private Set<Ingredient> ingredients = new HashSet<>();

    @OneToOne
    @MapsId
    private Item item;

    private BigDecimal defaultYield;


}
