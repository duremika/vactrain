package ru.duremika.vactrain.services;

import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.repositories.UserRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<User> findUser(String username) {
        return repository.findUserByUsername(username);
    }
}
