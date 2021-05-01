package pl.kamil.chefscookbook.food.domain.entity;

import pl.kamil.chefscookbook.food.domain.staticData.Unit;
import pl.kamil.chefscookbook.jpa.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Item extends BaseEntity {

    private String name;

    @ManyToOne
    private Unit unit;

    private BigDecimal pricePerUnit;
}
