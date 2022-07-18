package ru.duremika.vactrain.services;

import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.entities.VerificationToken;
import ru.duremika.vactrain.repositories.VerificationTokenRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;

@Service
public class VerificationTokenService {
    private final VerificationTokenRepository repository;

    public VerificationTokenService(VerificationTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public VerificationToken findByToken(String token) {
        return repository.findByToken(token);
    }

    @Transactional
    public VerificationToken findByUser(User user){
        return repository.findByUser(user);
    }

    public void remove(String token){
        VerificationToken verificationToken = findByToken(token);
        repository.delete(verificationToken);
    }

    public void save(User user, String token){
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationToken.setExpiryDate(createExpiryDate(24*60));
        repository.save(verificationToken);
    }

    private Timestamp createExpiryDate(int minutes){
        long now = System.currentTimeMillis();
        return new Timestamp(now + minutes * 1000L);
    }
}
