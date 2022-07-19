package ru.duremika.vactrain.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.entities.VerificationToken;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

@Service
public class EmailService {
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender javaMailSender;
    private final HttpServletRequest request;
    @Value("${spring.mail.username}")
    String from;


    public EmailService(
            VerificationTokenService verificationTokenService,
            JavaMailSender javaMailSender,
            HttpServletRequest request
    ) {
        this.verificationTokenService = verificationTokenService;
        this.javaMailSender = javaMailSender;
        this.request = request;
    }

    public void sendConfirmLink(User user) throws UnknownHostException {
        VerificationToken verificationToken = verificationTokenService.findByUser(user);
        if (verificationToken == null) return;

        String hostaddress;
        try {
            URL url = new URL(request.getRequestURL().toString());
            hostaddress = "%s://%s:%s".formatted(
                    url.getProtocol(), url.getHost(), url.getPort());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        String token = verificationToken.getToken();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(from);
        msg.setTo(user.getEmail());

        msg.setSubject("Welcome to Vactrain: Verify your account");
        msg.setText(
                """
                        Hello,

                        We are almost done creating your Vactrain account. To use the full potential of your account we kindly ask you to click on the link below to verify your account:

                        %s/activation?token=%s


                        If you did not register for a Vactrain Account, someone may have registered with your information by mistake. Contact Vactrain support for further assistance."""
                        .formatted(hostaddress, token)
        );
        javaMailSender.send(msg);
    }
}
