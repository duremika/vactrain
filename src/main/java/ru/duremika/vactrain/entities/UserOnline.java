package ru.duremika.vactrain.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserOnline {
    @Id
    private String username;

    public UserOnline() {
    }

    public UserOnline(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
