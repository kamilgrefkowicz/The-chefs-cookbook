package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findAllByChildItemId(Long id);
}
