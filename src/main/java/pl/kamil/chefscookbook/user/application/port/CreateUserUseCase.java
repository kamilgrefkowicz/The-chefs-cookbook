package pl.kamil.chefscookbook.user.application.port;

import lombok.Data;
import pl.kamil.chefscookbook.user.domain.UserEntity;
import pl.kamil.chefscookbook.shared.exception.NameAlreadyTakenException;
import pl.kamil.chefscookbook.user.registration.password_validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface CreateUserUseCase {

    UserEntity createNewUser(CreateUserCommand command) throws NameAlreadyTakenException;

    @Data
    @PasswordMatches
    class CreateUserCommand {
        @NotNull
        @NotEmpty
        private String username;
        @NotNull
        @NotEmpty
        private String password;
        private String passwordMatch;
    }
}
