package ru.duremika.vactrain.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.repositories.UserRepository;

import java.util.List;

@Controller
public class UserController {
    private final UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    public List<User> users() {
        return repository.findAll();
    }
}
