package ru.duremika.vactrain.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.UnknownHostException;

@Controller
public class LoginPageController {
    @GetMapping("/")
    public String index(Authentication authentication) throws UnknownHostException {
        return authentication == null ? "index" : "redirect:authenticated";
    }

    @GetMapping("/authenticated")
    public String authenticated() {
        return "authenticated";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        return authentication == null ? "login" : "redirect:authenticated";
    }

}
