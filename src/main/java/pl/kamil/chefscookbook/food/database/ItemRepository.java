package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kamil.chefscookbook.food.domain.entity.Item;
import pl.kamil.chefscookbook.food.domain.staticData.Type;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {


    List<Item> findAllByUserEntityId(Long userId);

    Optional<Item> findFirstByNameAndUserEntityId(String itemName, Long userEntityId);



    @Query("SELECT i from Item i where " +
            " (i.name like concat('%', :term, '%')) and  (i.userEntity.id = 1 or i.userEntity.id = :userId) and i.type <> pl.kamil.chefscookbook.food.domain.staticData.Type.DISH ")
    List<Item> findForAutocomplete(@Param("term") String term, @Param("userId") Long userId);

    @Query("select i from Item i where i.userEntity.id = :userId and i.type = pl.kamil.chefscookbook.food.domain.staticData.Type.DISH")
    List<Item> findAllDishesByUser(@Param("userId") Long userId);
}
