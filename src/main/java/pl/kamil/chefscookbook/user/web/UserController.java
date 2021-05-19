package pl.kamil.chefscookbook.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase;
import pl.kamil.chefscookbook.user.application.port.CreateUserUseCase.CreateUserCommand;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping("/new-user")
    public String showRegistrationForm(Model model) {
        CreateUserCommand command = new CreateUserCommand();
        model.addAttribute("user", command);
        return "user/registration";
    }

    @PostMapping("/register-user")
    public String registerUser(@Valid CreateUserCommand command) {

        return "redirect:/main-page";
    }
}
