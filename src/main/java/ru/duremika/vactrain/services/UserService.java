package ru.duremika.vactrain.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.duremika.vactrain.DTO.UserDTO;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.repositories.UserRepository;

import javax.transaction.Transactional;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;
    private final Logger logger;

    public UserService(
            UserRepository repository,
            ModelMapper modelMapper,
            BCryptPasswordEncoder passwordEncoder,
            VerificationTokenService verificationTokenService, EmailService emailService, Logger logger) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.logger = logger;
    }

    @Transactional
    public Optional<User> findUser(String username) {
        return repository.findUserByUsername(username);
    }

    public boolean isExists(String username) {
        Optional<User> user = findUser(username);
        logger.info("In user repository by username {} exists {}", username, user);
        return user.isPresent();
    }

    @Transactional
    public User save(User user) {
        User savedUser = repository.save(user);
        logger.info("User repository saved {}", user);
        logger.info("User repository after save return result {}", savedUser);
        return savedUser;
    }

    public void registry(UserDTO userDTO) {
        String encodedPassword = passwordEncoder
                .encode(userDTO.getPassword());

        User user = modelMapper.map(userDTO, User.class);

        user.setPassword(encodedPassword);
        user.setEnabled(false);

        User savedUser = save(user);

        if (savedUser != null){
            String token = UUID.randomUUID().toString();
            verificationTokenService.save(savedUser, token);
            emailService.sendConfirmLink(savedUser);
        } else {
            logger.info("New user not registry. DB returned null");
        }
    }
}
