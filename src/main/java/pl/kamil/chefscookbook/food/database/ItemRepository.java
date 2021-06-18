package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {


    @Query ("SELECT i from Item i left join fetch i.recipe where i.userEntity.id = :userId")
    List<Item> findAllByUserEntityId(@Param("userId") Long userId);

    Optional<Item> findFirstByNameAndUserEntityId(String itemName, Long userEntityId);


    // finds all basic and intermediate items that either belong to the user or belong to masteruser
    @Query("SELECT i from Item i where " +
            " (i.name like concat('%', :term, '%')) and  (i.userEntity.id = 1 or i.userEntity.id = :userId) and i.type <> pl.kamil.chefscookbook.food.domain.staticData.Type.DISH ")
    List<Item> findForAutocomplete(@Param("term") String term, @Param("userId") Long userId);

    @Query("select i from Item i where i.userEntity.id = :userId and i.type = pl.kamil.chefscookbook.food.domain.staticData.Type.DISH")
    List<Item> findAllDishesByUser(@Param("userId") Long userId);
}
