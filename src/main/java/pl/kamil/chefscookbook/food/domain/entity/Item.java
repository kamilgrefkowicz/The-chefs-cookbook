package pl.kamil.chefscookbook.food.domain.entity;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.staticData.Type;
import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.jpa.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    private String name;

    @ManyToOne(optional = false)
    private Unit unit;

    @ManyToOne(optional = false)
    private Type type;

    private BigDecimal pricePerUnit;
}
