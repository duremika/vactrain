package ru.duremika.vactrain.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.duremika.vactrain.services.UserOnlineService;

import java.util.List;

@RestController
public class UserOnlineController {
    private final UserOnlineService service;

    public UserOnlineController(UserOnlineService service) {
        this.service = service;
    }

    @GetMapping("/online")
    public List<String> getAll() {
        return service.getAll();
    }
}
