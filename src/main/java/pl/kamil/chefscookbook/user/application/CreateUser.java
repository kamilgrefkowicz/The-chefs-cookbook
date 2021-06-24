package pl.kamil.chefscookbook.user.application;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.application.port.FillTestUserWithRecipesUseCase;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import javax.transaction.Transactional;

import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.USER_NAME_TAKEN;

@Service
@AllArgsConstructor
public class CreateUser implements CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final FillTestUserWithRecipesUseCase fillTestUserWithRecipes;

    @Override
    @Transactional
    public Response<Void> execute(CreateUserCommand command)   {
        if (usernameTaken(command.getUsername())) return Response.failure(USER_NAME_TAKEN);

        UserEntity user = new UserEntity(command.getUsername(), encoder.encode(command.getPassword()));

        user = userRepository.save(user);

        if (command.isTestUser()) fillTestUserWithRecipes.execute(user);

        return Response.success(null);
    }

    private boolean usernameTaken(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
