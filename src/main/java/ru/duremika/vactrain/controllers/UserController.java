package ru.duremika.vactrain.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.duremika.vactrain.services.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<User> users() {
        return userService.findAll().stream().map((u) -> new User(u.getUsername(), u.isOnline())).toList();
    }

    record User(String username, boolean isOnline) {
    }
}
