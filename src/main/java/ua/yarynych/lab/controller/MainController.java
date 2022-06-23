package ua.yarynych.lab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String goHome() {
        return "account";
    }

    @GetMapping("/accessDenied")
    public String ex() {
        return "accessDenied";
    }


}