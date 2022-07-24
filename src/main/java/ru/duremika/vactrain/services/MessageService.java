package ru.duremika.vactrain.services;

import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.Message;
import ru.duremika.vactrain.repositories.MessageRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MessageService {
    private final MessageRepository repository;

    public MessageService(MessageRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public List<Message> getMessages(String username) {
        Set<Message> messages = new HashSet<>() {{
            addAll(repository.getMessagesByReceiver(username));
            addAll(repository.getMessagesBySender(username));
            addAll(repository.getMessagesByReceiverIsNull());
        }};
        return messages.stream().sorted(Comparator.comparing(Message::getDate)).toList();
    }

    @Transactional
    public void addMessage(Message message) {
        message.setDate(new Timestamp(System.currentTimeMillis()));
        repository.save(message);
    }
}
