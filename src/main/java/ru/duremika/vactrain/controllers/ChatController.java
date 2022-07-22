package ru.duremika.vactrain.controllers;

import org.slf4j.Logger;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.duremika.vactrain.entities.Message;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final Logger logger;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate, Logger logger) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.logger = logger;
    }


    @MessageMapping("/message") // */chat/message
    @SendTo("/chatroom/general")
    public Message receivePublicMessage(@Payload Message message){
        logger.info("Public message: {}", message);
        return message;
    }

    @MessageMapping("/private") // */chat/private
    public Message receivePrivateMessage(@Payload Message message){
        String receiver = message.getReceiver();
        simpMessagingTemplate.convertAndSendToUser( // */user/{username}/private
                receiver,
                "/private",
                message);

        logger.info("Private message: {}", message);
        return message;
    }
}
