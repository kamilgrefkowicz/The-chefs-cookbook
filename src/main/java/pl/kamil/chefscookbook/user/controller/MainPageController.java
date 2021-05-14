package pl.kamil.chefscookbook.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class MainPageController {

    @GetMapping({"", "/main-page", "/index"})
    public String home() {
        return "main-page";
    }


}
