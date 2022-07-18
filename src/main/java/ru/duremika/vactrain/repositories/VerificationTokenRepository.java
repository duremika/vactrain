package ru.duremika.vactrain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);
}
