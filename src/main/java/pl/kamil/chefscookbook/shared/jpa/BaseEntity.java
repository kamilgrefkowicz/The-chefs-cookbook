package pl.kamil.chefscookbook.shared.jpa;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode(of="uuid")
@ToString
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private Long version;

    private String uuid = UUID.randomUUID().toString();


}
