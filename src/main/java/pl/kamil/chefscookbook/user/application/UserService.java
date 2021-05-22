package pl.kamil.chefscookbook.user.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;

@Service
@AllArgsConstructor
public class UserService implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public UserEntity createNewUser(CreateUserCommand command) throws NameAlreadyTakenException {
        if (usernameTaken(command.getUsername())) throw new NameAlreadyTakenException("There is an account with username " + command.getUsername() + " already.");

        UserEntity user = new UserEntity(command.getUsername(), encoder.encode(command.getPassword()));
        return userRepository.save(user);
    }

    private boolean usernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
