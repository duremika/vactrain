package ru.duremika.vactrain.entities;

public class Message {
    private String sender;
    private String receiver;
    private String messageText;
    private String date;
    private Status status;

    public Message() {
    }

    public Message(String sender, String receiver, String messageText, String date, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.date = date;
        this.status = status;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
