package pl.kamil.chefscookbook.shared.jpa;

import lombok.Getter;
import lombok.Setter;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class OwnedEntity extends BaseEntity{

    @ManyToOne(optional = false)
    protected UserEntity userEntity;

}
