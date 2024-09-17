package ru.tatarinov.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import ru.tatarinov.taskmanager.exception.ObjectNotFoundException;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.repository.UserRepository;

import java.util.Optional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws ObjectNotFoundException {
        Optional<User> user = userRepository.getUserByMail(mail);
        if (user.isPresent()){
            return user.get();
        }
        else
            throw new ObjectNotFoundException("User not found");
    }
}
