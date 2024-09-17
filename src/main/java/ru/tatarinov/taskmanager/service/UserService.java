package ru.tatarinov.taskmanager.service;

import ru.tatarinov.taskmanager.DTO.UserDTO;
import ru.tatarinov.taskmanager.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(int id);
    Optional<User> getUserByMail(String mail);
    Optional<User> getUserByUsername(String username);
    boolean userLogin(String mail, String password);
    void createUser(UserDTO userDTO);
}
