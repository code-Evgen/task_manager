package ru.tatarinov.effectivemobile.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.effectivemobile.model.User;
import ru.tatarinov.effectivemobile.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(int id){
        return userRepository.findById(id);
    }

    public Optional<User> getUserByMail(String mail){
        return userRepository.getUserByMail(mail);
    }

    public Optional<User> getUserByUsername(String username){
        return userRepository.getUserByUsername(username);
    }

    public boolean userLogin(String mail, String password) {
        Optional<User> userOptional = userRepository.getUserByMail(mail);
        if (userOptional.isEmpty())
            throw new UsernameNotFoundException("Email not found");

        User user = userOptional.get();
        return user.getPassword().equals(password);
    }

    @Transactional
    public void createUser(User user){
        userRepository.save(user);
    }
}
