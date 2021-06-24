package pl.kamil.chefscookbook.user.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.USER_CREATION_SUCCESSFUL;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.USER_NEW;

@WebMvcTest({UserController.class})
@ActiveProfiles("test")
@WithMockUser
class UserControllerTest {

    @MockBean
    CreateUserUseCase createUser;

    @Autowired
    MockMvc mockMvc;

    @Test
    void gettingNewUserRegistrationFormReturnsCorrectMav() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/register-user"))

                .andExpect(model().attributeExists("createUserCommand"))
                .andExpect(view().name(USER_NEW));
    }
    @Test
    void creatingUserWithInvalidNameShouldReturnValidationError() throws Exception {

        mockMvc.perform(getPostRequestForCreateUser("aa"))

                .andExpect(model().attributeHasErrors("createUserCommand"))
                .andExpect(view().name(USER_NEW));
    }
    @Test
    void creatingUserWithNoMatchingPasswordsShouldReturnValidationError() throws Exception {
        mockMvc.perform(getPostRequestForCreateUser("test", "test1"))

                .andExpect(model().attributeHasErrors("createUserCommand"))
                .andExpect(view().name(USER_NEW));
    }
    @Test
    void unsuccessfulUserCreationShouldReturnCorrectMav() throws Exception {
        when(createUser.execute(any())).thenReturn(Response.failure("test"));

        mockMvc.perform(getPostRequestForCreateUser())

                .andExpect(model().attribute("error", "test"))
                .andExpect(view().name(USER_NEW));
    }
    @Test
    void registeringUserWithValidDataShouldCallServiceToSave() throws Exception {
        when(createUser.execute(any())).thenReturn(Response.failure("test"));

        mockMvc.perform(getPostRequestForCreateUser());

        verify(createUser).execute(any());
    }
    @Test
    void successfullyCreatingUserShouldReturnCorrectMav() throws Exception {
        when(createUser.execute(any())).thenReturn(Response.success(null));

        mockMvc.perform(getPostRequestForCreateUser())

                .andExpect(model().attribute("message", USER_CREATION_SUCCESSFUL))
                .andExpect(view().name("main-page"));
    }

    private RequestBuilder getPostRequestForCreateUser(String password, String passwordMatch) {
        return post("/user/register-user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "test")
                .param("password", password)
                .param("passwordMatch", passwordMatch)
                .with(csrf());
    }
    private RequestBuilder getPostRequestForCreateUser() {
        return post("/user/register-user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", "test")
                .param("password", "test")
                .param("passwordMatch", "test")
                .with(csrf());
    }


    private MockHttpServletRequestBuilder getPostRequestForCreateUser(String username) {
        return post("/user/register-user")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", username)
                .param("password", "xxx")
                .param("passwordMatch", "xxx")
                .with(csrf());
    }
}


















