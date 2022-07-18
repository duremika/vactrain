package ru.duremika.vactrain.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.entities.VerificationToken;

@Service
public class EmailService {
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    String from;


    public EmailService(VerificationTokenService verificationTokenService, JavaMailSender javaMailSender) {
        this.verificationTokenService = verificationTokenService;
        this.javaMailSender = javaMailSender;
    }

    public void sendConfirmLink(User user) {
        VerificationToken verificationToken = verificationTokenService.findByUser(user);
        if (verificationToken == null) return;

        String token = verificationToken.getToken();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(user.getEmail());

        msg.setSubject("Welcome to Vactrain: Verify your account");
        msg.setText(
                """
                        Hello,

                        We are almost done creating your Vactrain account. To use the full potential of your account we kindly ask you to click on the link below to verify your account:

                        http://localhost:8080/activation?token=%s


                        If you did not register for a Vactrain Account, someone may have registered with your information by mistake. Contact Vactrain support for further assistance."""
                        .formatted(token)
        );
        javaMailSender.send(msg);
    }
}
