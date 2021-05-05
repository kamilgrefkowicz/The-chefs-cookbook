package pl.kamil.chefscookbook.food.database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.chefscookbook.food.domain.entity.Ingredient;

public interface IngredientJpaRepository extends JpaRepository<Ingredient, Long> {
}
