package pl.kamil.chefscookbook.user.application;

import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.domain.UserEntity;

@Service
public class UserService implements CreateUserUseCase {
    @Override
    public Long createNewUser(CreateUserCommand command) {

        return null;
    }
}
