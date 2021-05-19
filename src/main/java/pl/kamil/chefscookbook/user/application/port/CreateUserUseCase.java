package pl.kamil.chefscookbook.user.application.port;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public interface CreateUserUseCase {

    Long createNewUser(CreateUserCommand command);

    @Data
    class CreateUserCommand {
        @NotNull
        @NotEmpty
        private String username;
        @NotNull
        @NotEmpty
        private String password;
        @NotNull
        @NotEmpty
        private String passwordMatch;
    }
}
