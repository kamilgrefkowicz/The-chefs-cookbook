package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Item;


public interface ItemJpaRepository extends JpaRepository<Item, Long> {


}
