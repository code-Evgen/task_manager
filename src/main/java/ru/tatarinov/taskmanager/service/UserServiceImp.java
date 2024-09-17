package ru.tatarinov.taskmanager.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.taskmanager.DTO.UserDTO;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.repository.UserRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImp implements UserService{
    public final UserRepository userRepository;

    public UserServiceImp(UserRepository userRepository) {
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
    public void createUser(UserDTO userDTO){
        ModelMapper requestMapper = new ModelMapper();
        User user = requestMapper.map(userDTO, User.class);

        userRepository.save(user);
    }
}
