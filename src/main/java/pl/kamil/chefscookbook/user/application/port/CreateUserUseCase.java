package pl.kamil.chefscookbook.user.application.port;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.registration.password_validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CreateUserUseCase {

    Response<Void> createNewUser(CreateUserCommand command) ;

    @Data
    @PasswordMatches
    @NoArgsConstructor
    @AllArgsConstructor
    class CreateUserCommand {

        @NotNull
        @Size(min=3, max=15)
        private String username;
        @NotNull
        @NotEmpty
        @Length(min=3, max=15)
        private String password;
        private String passwordMatch;
        private boolean testUser;
    }
}
