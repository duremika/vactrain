package ru.duremika.vactrain.controllers;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.duremika.vactrain.entities.Message;
import ru.duremika.vactrain.services.MessageService;

import java.util.List;

@RestController
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/messages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> getMessages(Authentication authentication) {
        return messageService.getMessages(authentication.getName());
    }
}
