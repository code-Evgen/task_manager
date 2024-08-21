package ru.tatarinov.effectivemobile.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.tatarinov.effectivemobile.DTO.TaskDTO;
import ru.tatarinov.effectivemobile.service.UserService;

@Component
public class TaskValidation implements Validator {
    private final UserService userService;

    public TaskValidation(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TaskDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TaskDTO taskDTO = (TaskDTO) target;
        if (taskDTO.getOwnerId() != null && userService.getUserById(taskDTO.getOwnerId()).isEmpty()){
            errors.rejectValue("ownerId", "", "User id not found");
        }
        if (taskDTO.getExecutorId() != null && userService.getUserById(taskDTO.getExecutorId()).isEmpty()){
            errors.rejectValue("executorId", "", "User id not found");
        }
    }
}
