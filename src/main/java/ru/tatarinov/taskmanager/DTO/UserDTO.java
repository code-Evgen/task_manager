package ru.tatarinov.taskmanager.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class UserDTO {
    @NotNull(message = "field is required")
    @Schema(name = "username", example = "user-1", required = true)
    private String username;
    @NotNull(message = "field is required")
    @Schema(name = "mail", example = "examle@mail.com", required = true)
    private String mail;
    @NotNull(message = "field is required")
    @Schema(name = "password", example = "P@ssw0rd", required = true)
    private String password;

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
