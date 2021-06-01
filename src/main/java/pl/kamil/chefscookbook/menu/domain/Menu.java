package pl.kamil.chefscookbook.menu.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Menu extends BaseEntity {

    private String name;

    @ManyToOne
    private UserEntity userEntity;

    @ManyToMany
    @JoinTable(
            name = "menu_items",
            joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")})
    @Builder.Default
    private Set<Item> items = new HashSet<>();



}
