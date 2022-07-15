package ru.duremika.vactrain.services;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.duremika.vactrain.DTO.UserDTO;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.repositories.UserRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(
            UserRepository repository,
            ModelMapper modelMapper,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
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
        user.setEnabled(true);
        user.setCreatedAt(new Timestamp(new Date().getTime()));
        save(user);
    }
}
