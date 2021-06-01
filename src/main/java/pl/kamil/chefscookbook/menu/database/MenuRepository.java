package pl.kamil.chefscookbook.menu.database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.chefscookbook.menu.application.dto.PoorMenu;
import pl.kamil.chefscookbook.menu.domain.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findAllByUserEntityId(Long valueOf);

    Optional<Menu> findByNameAndUserEntityId(String menuName, Long valueOf);
}
