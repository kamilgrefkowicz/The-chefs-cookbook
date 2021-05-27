package pl.kamil.chefscookbook.user.domain;

import lombok.*;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.jpa.BaseEntity;

import javax.persistence.*;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "users")
public class UserEntity extends BaseEntity{

    private String username;

    private String password;

}
