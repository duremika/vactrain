package ru.duremika.vactrain.services;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.entities.VerificationToken;

import javax.servlet.http.HttpServletRequest;

@Service
public class EmailService {
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;
    private final HttpServletRequest request;
    private final Logger logger;
    @Value("${spring.mail.username}")
    String from;


    public EmailService(
            VerificationTokenService verificationTokenService,
            JavaMailSender javaMailSender,
            HttpServletRequest request,
            Logger logger) {
        this.verificationTokenService = verificationTokenService;
        this.javaMailSender = javaMailSender;
        this.request = request;
        this.logger = logger;
    }

    public void sendConfirmLink(User user){
        VerificationToken verificationToken = verificationTokenService.findByUser(user);
        if (verificationToken == null){
            logger.info("Send link failed. DB returned null");
            return;
        }

        String token = verificationToken.getToken();
        String requestUrl = request.getRequestURL().toString();
        String hostUrl = requestUrl.replaceFirst("/register", "");
        String confirmLink = "%s/activation?token=%s".formatted(hostUrl, token);
        logger.info("Confirm link: {}", confirmLink);

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(user.getEmail());

        msg.setSubject("Welcome to Vactrain: Verify your account");
        msg.setText(
                """
                        Hello,

                        We are almost done creating your Vactrain account. To use the full potential of your account we kindly ask you to click on the link below to verify your account:

                        %s

                        If you did not register for a Vactrain Account, someone may have registered with your information by mistake. Contact Vactrain support for further assistance."""
                        .formatted(confirmLink)
        );
        try {
            javaMailSender.send(msg);
        } catch (MailException e){
            logger.error("Message: {} dont has ben send", msg);
        }
    }
}
