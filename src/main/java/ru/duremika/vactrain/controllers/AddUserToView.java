package ru.duremika.vactrain.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.duremika.vactrain.entities.User;

@ControllerAdvice
public class AddUserToView {

    @ModelAttribute("user")
    public User addUserToModel(Authentication authentication) {
        return authentication != null ? (User) authentication.getPrincipal() : null;
    }
}
