package ru.duremika.vactrain.entities;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String sender;
    private String receiver;
    private String messageText;
    private Timestamp date;
    private Status status;



    public Message() {
    }

    public Message(String sender, String receiver, String messageText, Timestamp date, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.date = date;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Message{sender=%s, receiver=%s, messageText=%s, date=%s, status=%s}"
                .formatted(sender, receiver, messageText, date, status);
    }

    public enum Status {
        JOIN,
        MESSAGE,
        LEAVE
    }
}
