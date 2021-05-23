package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.kamil.chefscookbook.food.application.dto.item.ItemAutocompleteDto;
import pl.kamil.chefscookbook.food.domain.entity.Item;

import java.util.List;
import java.util.Optional;


public interface ItemJpaRepository extends JpaRepository<Item, Long> {

    Item findFirstByNameContaining(String term);

    List<Item> findAllByUserEntityId(Long userId);

    Optional<Item> findFirstByNameAndUserEntityId(String itemName, Long userEntityId);


    @Query("SELECT i from Item i where " +
            " (i.name like concat('%', :term, '%')) and  (i.userEntity.id = 1 or i.userEntity.id = :userId) and i.type.id <> 3")
    List<Item> findForAutocomplete(@Param("term") String term, @Param("userId") Long userId);
}
