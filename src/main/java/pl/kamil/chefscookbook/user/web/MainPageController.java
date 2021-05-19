package pl.kamil.chefscookbook.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class MainPageController {


    @GetMapping({"", "/main-page", "/index"})
    public String home() {
        return "main-page";
    }
}
