package ru.tatarinov.taskmanager.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tatarinov.taskmanager.DTO.UserDTO;
import ru.tatarinov.taskmanager.service.UserServiceImp;

@Component
public class RegistrationValidation implements Validator {
    private final UserServiceImp userService;

    public RegistrationValidation(UserServiceImp userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userService.getUserByUsername(userDTO.getUsername()).isPresent()){
            errors.rejectValue("username", "", "This username already exist");
        }
        if (userService.getUserByMail(userDTO.getMail()).isPresent()){
            errors.rejectValue("mail", "", "This mail already exist");
        }
    }
}
