package pl.kamil.chefscookbook.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase.CreateUserCommand;
import pl.kamil.chefscookbook.user.database.UserRepository;
import pl.kamil.chefscookbook.user.domain.UserEntity;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.USER_NAME_TAKEN;

@ExtendWith(MockitoExtension.class)
class CreateUserTest {

    @Mock
    UserRepository userRepository;

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    CreateUser createUser;

    @Captor
    ArgumentCaptor<UserEntity> userEntityCaptor;

    @BeforeEach
    void setUp() {
        createUser = new CreateUser(userRepository, passwordEncoder);
    }

    @Test
    void creatingNewUserShouldCallRepositoryToCheckIfNameTaken(){
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserEntity()));
        CreateUserCommand command = getCommand("testUsername");

        createUser.createNewUser(command);

        verify(userRepository).findByUsername("testUsername");
    }

    @Test
    void creatingUserWhenNameTakenShouldReturnFailure() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new UserEntity()));
        CreateUserCommand command = getCommand("testUsername");

        Response<Void> response = createUser.createNewUser(command);

        assertFalse(response.isSuccess());
        assertThat(response.getError(), equalTo(USER_NAME_TAKEN));
    }
    @Test
    void creatingValidUserShouldCallRepositoryToSave() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        CreateUserCommand command = getCommand("testUsername");

        createUser.createNewUser(command);

        verify(userRepository).save(userEntityCaptor.capture());
        UserEntity saved = userEntityCaptor.getValue();
        assertThat(saved.getUsername(), equalTo("testUsername"));
        assertTrue(passwordEncoder.matches("test", saved.getPassword()));
    }
    @Test
    void creatingValidUserShouldReturnSuccessfulResponse() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        CreateUserCommand command = getCommand("testUsername");

        Response<Void> response = createUser.createNewUser(command);

        assertTrue(response.isSuccess());
    }

    private CreateUserCommand getCommand(String username) {
        return new CreateUserCommand(username, "test", "test");
    }
}