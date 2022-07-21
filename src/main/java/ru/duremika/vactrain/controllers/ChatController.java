package ru.duremika.vactrain.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.duremika.vactrain.entities.Message;

@Controller
public class ChatController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    @MessageMapping("/message") // */app/message
    @SendTo("/chatroom/general")
    public Message receivePublicMessage(@Payload Message message){
        return message;
    }

    @MessageMapping("/private") // */user/{username}/private
    public Message receivePrivateMessage(@Payload Message message){
        String receiver = message.getReceiver();
        simpMessagingTemplate.convertAndSendToUser(
                receiver,
                "/private",
                message);
        return message;
    }
}
