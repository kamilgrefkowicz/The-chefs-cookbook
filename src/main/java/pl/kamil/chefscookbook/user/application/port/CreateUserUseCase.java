package pl.kamil.chefscookbook.user.application.port;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.domain.UserEntity;
import pl.kamil.chefscookbook.user.registration.password_validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface CreateUserUseCase {

    Response<UserEntity> createNewUser(CreateUserCommand command) ;

    @Data
    @PasswordMatches
    class CreateUserCommand {
        @NotNull
        @NotEmpty
        @Length(min=3, max=15)
        private String username;
        @NotNull
        @NotEmpty
        @Length(min=3, max=15)
        private String password;
        private String passwordMatch;
    }
}
