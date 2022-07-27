package ru.duremika.vactrain.services;

import org.springframework.stereotype.Service;
import ru.duremika.vactrain.entities.Message;
import ru.duremika.vactrain.repositories.MessageRepository;

import javax.transaction.Transactional;
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
    public List<Message> getMessages(String username, String interlocutor) {
        if (interlocutor.equals("general")) {
            return repository.getMessagesByReceiverIsNull()
                    .stream().sorted(
                            Comparator.comparing(Message::getDate)
                    ).toList();
        } else {
            Set<Message> messages = new HashSet<>() {{
                addAll(repository.getMessagesByReceiverAndSender(username, interlocutor));
                addAll(repository.getMessagesByReceiverAndSender(interlocutor, username));
            }};
            return messages.stream().sorted(Comparator.comparing(Message::getDate)).toList();
        }


    }

    @Transactional
    public void addMessage(Message message) {
        repository.save(message);
    }
}
