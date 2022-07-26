package ru.duremika.vactrain.services;

import org.slf4j.Logger;
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
        repository.save(new UserOnline(username));
        logger.info("User {} now online", username);
    }

    @Transactional
    public void setOffline(String username) {
        repository.delete(new UserOnline(username));
        logger.info("User {} now offline", username);
    }

    public List<String> getAll() {
        return repository.findAll().stream().map(UserOnline::getUsername).toList();
    }
}
