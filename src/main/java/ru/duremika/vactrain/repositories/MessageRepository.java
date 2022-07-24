package ru.duremika.vactrain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.duremika.vactrain.entities.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getMessagesByReceiver(String receiver);

    List<Message> getMessagesBySender(String sender);

    List<Message> getMessagesByReceiverIsNull();
}
