package pl.kamil.chefscookbook.menu.domain;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.OwnedEntity;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Menu extends OwnedEntity {

    private String name;


    @ManyToMany
    @JoinTable(
            name = "menu_items",
            joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")})
    @Builder.Default
    private Set<Item> items = new HashSet<>();

    public Menu(String name, UserEntity userEntity) {
        this.name = name;
        this.userEntity = userEntity;
        this.items = new HashSet<>();
    }

    public void addItemsToMenu(Set<Item> itemsToAdd) {
        this.items.addAll(itemsToAdd);
    }

    public void removeItem(Item toRemove) {
        this.items.remove(toRemove);
    }
}
