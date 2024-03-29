package ru.duremika.vactrain.controllers;

import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import ru.duremika.vactrain.entities.Message;
import ru.duremika.vactrain.entities.User;
import ru.duremika.vactrain.services.MessageService;
import ru.duremika.vactrain.services.UserService;

import java.sql.Timestamp;
import java.util.Objects;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final UserService userService;
    private final Logger logger;

    public ChatController(
            SimpMessagingTemplate simpMessagingTemplate,
            MessageService messageService,
            UserService userService, Logger logger
    ) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.messageService = messageService;
        this.userService = userService;
        this.logger = logger;
    }


    @MessageMapping("/message") // */chat/message
    @SendTo("/chatroom/general")
    public Message receivePublicMessage(@Payload Message message) {
        if (message.getMessageText() != null && message.getMessageText().trim().isEmpty()) {
            return null;
        }
        message.setDate(new Timestamp(System.currentTimeMillis()));

        switch (message.getStatus()) {
            case MESSAGE -> messageService.addMessage(message);
            case JOIN -> userService.setOnline(message.getSender());
            case LEAVE -> userService.setOffline(message.getSender());
        }
        logger.info("Public message: {}", message);
        return message;
    }

    @MessageMapping("/private") // */chat/private
    public Message receivePrivateMessage(@Payload Message message) {
        if (message.getMessageText().trim().isEmpty()) {
            return null;
        }
        message.setDate(new Timestamp(System.currentTimeMillis()));
        String receiver = message.getReceiver();
        simpMessagingTemplate.convertAndSendToUser( // */user/{username}/private
                receiver,
                "/private",
                message);
        if (message.getStatus() == Message.Status.MESSAGE) {
            messageService.addMessage(message);
        }
        logger.info("Private message: {}", message);
        return message;
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event) {
        String username = ((User) ((Authentication) Objects.requireNonNull(event.getUser())).getPrincipal()).getUsername();
        userService.setOffline(username);
        logger.info("Client with username {} disconnected", username);
    }
}
