package pl.kamil.chefscookbook.shared.jpa;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(of="uuid")
@ToString
public abstract class BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private String uuid = UUID.randomUUID().toString();
}
