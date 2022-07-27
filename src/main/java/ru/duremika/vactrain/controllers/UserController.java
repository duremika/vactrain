package ru.duremika.vactrain.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.duremika.vactrain.services.UserOnlineService;
import ru.duremika.vactrain.services.UserService;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;
    private final UserOnlineService userOnlineService;

    public UserController(
            UserService userService,
            UserOnlineService userOnlineService
    ) {
        this.userService = userService;
        this.userOnlineService = userOnlineService;
    }

    @GetMapping("/users")
    public List<User> users() {
        List<String> users = userService.findAll();
        List<String> onlineUsers = userOnlineService.getAll();

        return users.stream().map(
                usr -> new User(usr, onlineUsers.contains(usr))
        ).toList();
    }

    record User(String username, boolean isOnline) {
    }
}
