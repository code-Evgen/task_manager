package ru.tatarinov.taskmanager.DTO;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import ru.tatarinov.taskmanager.exception.ObjectNotFoundException;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.service.UserServiceImp;

import java.util.Optional;

@Component
public class TaskDTOToTaskConverter implements Converter<TaskDTO, Task> {
    private final UserServiceImp userService;

    public TaskDTOToTaskConverter(UserServiceImp userService) {
        this.userService = userService;
    }

    @Override
    public Task convert(MappingContext<TaskDTO, Task> mappingContext) {
        Task task = mappingContext.getDestination();
        TaskDTO taskDTO = mappingContext.getSource();
        if (task == null) {
            task = new Task();
        }
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setTaskState(taskDTO.getTaskState());
        task.setTaskPriority(taskDTO.getTaskPriority());
        if (taskDTO.getOwnerId() != null) {
            Optional<User> ownerOptional = userService.getUserById(taskDTO.getOwnerId());
            if (ownerOptional.isEmpty())
                throw new ObjectNotFoundException("User not found");
            User owner = ownerOptional.get();
            task.setOwner(owner);
        }
        else
            task.setOwner(null);

        if (taskDTO.getExecutorId() != null) {
            Optional<User> executorOptional = userService.getUserById(taskDTO.getExecutorId());
            if (executorOptional.isEmpty())
                throw new ObjectNotFoundException("User not found");
            User executor = executorOptional.get();
            task.setExecutor(executor);
        }
        else
            task.setExecutor(null);

        return task;
    }
}
