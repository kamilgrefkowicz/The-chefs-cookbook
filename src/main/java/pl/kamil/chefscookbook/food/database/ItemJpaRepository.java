package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kamil.chefscookbook.food.application.port.QueryItemUseCase;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.shared.response.Response;

import java.util.List;
import java.util.Optional;


public interface ItemJpaRepository extends JpaRepository<Item, Long> {


}
