package ru.duremika.vactrain.services;

import org.modelmapper.ModelMapper;
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

    public UserService(
            UserRepository repository,
            ModelMapper modelMapper,
            BCryptPasswordEncoder passwordEncoder,
            VerificationTokenService verificationTokenService, EmailService emailService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @Transactional
    public Optional<User> findUser(String username) {
        return repository.findUserByUsername(username);
    }

    public boolean isExists(String username) {
        return findUser(username).isPresent();
    }

    @Transactional
    public User save(User user) {
        return repository.save(user);
    }

    public void registry(UserDTO userDTO) {
        String encodedPassword = passwordEncoder
                .encode(userDTO.getPassword());

        User user = modelMapper.map(userDTO, User.class);

        user.setPassword(encodedPassword);
        user.setEnabled(false);
        Optional<User> savedUser = Optional.of(save(user));
        savedUser.ifPresent( u -> {
            String token = UUID.randomUUID().toString();
            verificationTokenService.save(savedUser.get(), token);
            try {
                emailService.sendConfirmLink(u);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
