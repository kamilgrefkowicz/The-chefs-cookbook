package pl.kamil.chefscookbook.user.database;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameIgnoreCase(String username);

    Optional<UserEntity> findByUsername(String username);
}
