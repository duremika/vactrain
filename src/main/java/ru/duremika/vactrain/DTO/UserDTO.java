package ru.duremika.vactrain.DTO;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public  class UserDTO {
    @NotBlank(message = "Enter username")
    private  String username;

    @NotBlank(message = "Enter your email")
    @Email(message = "Enter a valid email address")
    private  String email;
    @NotBlank(message = "Enter your first name")
    private  String firstname;
    @NotBlank(message = "Enter your last name")
    private  String lastname;
    @NotBlank(message = "Enter password")
    @Length(min = 6, message = "Password must be at least 6 characters")
    private  String password;
    @NotBlank(message = "Re-enter password")
    private  String rpassword;

    public UserDTO(
            String username,
            String email,
            String firstname,
            String lastname,
            String password,
            String rpassword) {
        this.username = username;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.rpassword = rpassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRpassword() {
        return rpassword;
    }

    public void setRpassword(String rpassword) {
        this.rpassword = rpassword;
    }

    @Override
    public String toString() {
        return String.format("UserDTO{username='%s', email='%s', firstname='%s', lastname='%s', password='%s', rpassword='%s'}",
                username, email, firstname, lastname, password, rpassword);
    }
}