package ru.duremika.vactrain.DTO;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public record UserDTO(
        @NotBlank(message = "Enter username")
        String username,
        @NotBlank(message = "Enter your email")
        @Email(message = "Enter a valid email address")
        String email,
        @NotBlank(message = "Enter your first name")
        String firstname,
        @NotBlank(message = "Enter your last name")
        String lastname,
        @NotBlank(message = "Enter password")
        @Length(min = 6, message = "Password must be at least 6 characters")
        String password,
        @NotBlank(message = "Re-enter password")
        String rpassword) {
    @Override
    public String toString() {
        return String.format("UserDTO{username='%s', email='%s', firstname='%s', lastname='%s', password='%s', rpassword='%s'}",
                username, email, firstname, lastname, password, rpassword);
    }
}