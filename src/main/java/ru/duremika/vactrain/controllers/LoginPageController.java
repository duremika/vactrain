package ru.duremika.vactrain.controllers;

import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    private final Logger logger;

    public LoginPageController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping("/")
    public String index(Authentication authentication) {
        logger.info("Get request '/' authentication: {}", authentication);
        return authentication == null ? "index" : "redirect:authenticated";
    }

    @GetMapping("/authenticated")
    public String authenticated() {
        logger.info("Get request '/authenticated'");
        return "authenticated";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {
        logger.info("Get request '/login' authentication: {}", authentication);
        return authentication == null ? "login" : "redirect:authenticated";
    }

}
