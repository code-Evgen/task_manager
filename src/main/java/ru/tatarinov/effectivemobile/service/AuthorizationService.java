package ru.tatarinov.effectivemobile.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.tatarinov.effectivemobile.model.Task;
import ru.tatarinov.effectivemobile.model.User;

import java.util.List;

@Service
public class AuthorizationService {
    private final TaskService taskService;

    public AuthorizationService(TaskService taskService) {
        this.taskService = taskService;
    }

    public boolean ownerAuthorization(int taskId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> taskList = taskService.getTaskListByOwnerId(user.getId());

        for (Task task: taskList){
            if (task.getId() == taskId)
                return true;
        }
        return false;
    }

    public boolean executorAuthorization(int taskId){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Task> taskList = taskService.getTaskListByExecutorId(user.getId());

        for (Task task: taskList){
            if (task.getId() == taskId)
                return true;
        }
        return false;
    }
}
