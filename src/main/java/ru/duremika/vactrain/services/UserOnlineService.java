package ru.duremika.vactrain.services;

import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.UserOnline;
import ru.duremika.vactrain.repositories.UserOnlineRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserOnlineService {
    private final UserOnlineRepository repository;
    private final Logger logger;

    public UserOnlineService(
            UserOnlineRepository repository,
            Logger logger
    ) {
        this.repository = repository;
        this.logger = logger;
    }

    @Transactional
    public void setOnline(String username) {
        repository.saveAndFlush(new UserOnline(username));
        logger.info("User {} now online", username);
    }

    @Transactional
    public void setOffline(String username) {
        List<String> usersOnline = getAll();
        if (usersOnline.contains(username)) {
            repository.deleteByUsername(username);
            logger.info("User {} now offline", username);
        }
    }

    @Transactional
    public List<String> getAll() {
        return repository.findAll().stream().map(UserOnline::getUsername).toList();
    }
}
