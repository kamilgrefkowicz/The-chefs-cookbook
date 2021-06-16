package pl.kamil.chefscookbook.user.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.shared.response.Response;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase.CreateUserCommand;

import javax.validation.Valid;

import static pl.kamil.chefscookbook.shared.string_values.MessageValueHolder.USER_CREATION_SUCCESSFUL;
import static pl.kamil.chefscookbook.shared.string_values.UrlValueHolder.USER_NEW;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final CreateUserUseCase userService;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute("user", new CreateUserCommand());
    }

    @GetMapping("/register-user")
    public String showRegistrationForm() {

        return USER_NEW;
    }

    @PostMapping("/register-user")
    public String registerUser(Model model, @Valid CreateUserCommand command, BindingResult result) {

        if (result.hasErrors()) {
            return USER_NEW;
        }

        Response<Void> userCreation = userService.createNewUser(command);

        if (!userCreation.isSuccess()) {
            model.addAttribute("message", userCreation.getError());
            return USER_NEW;
        }


        model.addAttribute("message", USER_CREATION_SUCCESSFUL);

        return "main-page";
    }
}
