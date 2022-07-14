package ru.duremika.vactrain.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.duremika.vactrain.entities.User;

@Controller
public class LoginPageController {
    @GetMapping("/")
    public String index(Model model) {
        if (getPrincipal() != null) {
            model.addAttribute("user", getPrincipal());
            return "authenticated";
        }
        return "index";
    }

    @GetMapping("/authenticated")
    public String authenticated(Model model) {
        model.addAttribute("user", getPrincipal());
        return "authenticated";
    }

    @GetMapping("/login")
    public String login() {
        return getPrincipal() == null ? "login" : "redirect:authenticated";
    }

    private User getPrincipal() {
        User user = null;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User) {
            user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return user;
    }
}
