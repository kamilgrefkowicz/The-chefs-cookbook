package pl.kamil.chefscookbook.user.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.USER_NAME_TAKEN;

@Service
@AllArgsConstructor
public class CreateUser implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public Response<Void> createNewUser(CreateUserCommand command)   {
        if (usernameTaken(command.getUsername())) return Response.failure(USER_NAME_TAKEN);

        UserEntity user = new UserEntity(command.getUsername(), encoder.encode(command.getPassword()));

        userRepository.save(user);
        return Response.success(null);
    }

    private boolean usernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
