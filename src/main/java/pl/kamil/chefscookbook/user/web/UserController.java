package pl.kamil.chefscookbook.user.web;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase.CreateUserCommand;
import pl.kamil.chefscookbook.user.domain.UserEntity;
import pl.kamil.chefscookbook.user.registration.UserAlreadyExistsException;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {

    private final CreateUserUseCase userService;

    @GetMapping("/new-user")
    public String showRegistrationForm(Model model) {
        CreateUserCommand command = new CreateUserCommand();
        model.addAttribute("user", command);
        return "user/registration";
    }

    @PostMapping("/register-user")
    public String registerUser(Model model, @Valid CreateUserCommand command, BindingResult result) {

        if (result.hasErrors()) return "user/registration";

        try {
            UserEntity user = userService.createNewUser(command);
        } catch (UserAlreadyExistsException e) {
            model.addAttribute("message", "An account with this username already exists.");
            return "/user/registration";
        }
        model.addAttribute("message", "Your account has been created");

        return "main-page";
    }
}
