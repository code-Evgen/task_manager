package ru.tatarinov.taskmanager.service;

import ru.tatarinov.taskmanager.DTO.TaskDTO;
import ru.tatarinov.taskmanager.DTO.TaskDTOResponse;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.model.TaskState;

import java.util.List;

public interface TaskService {
    void createTask(TaskDTO taskDTO);
    Task getTaskById(int id);
    TaskDTOResponse getTaskDTOById(int id);
    List<TaskDTOResponse> getTaskDTOListByOwnerId(int ownerId);
    List<Task> getTaskListByOwnerId(int ownerId);
    List<TaskDTOResponse> getTaskDTOListByOwnerId(int ownerId, int page, int tasksPerPage);
    List<TaskDTOResponse> getTaskDTOListByExecutorId(int executorId);
    List<TaskDTOResponse> getTaskDTOListByExecutorId(int executorId, int page, int tasksPerPage);
    List<Task> getTaskListByExecutorId(int executorId);
    void deleteTask(int taskId);
    boolean changeTaskStatus(int taskId, TaskState newTaskState);
    boolean setExecutor(int taskId, int executorId);
    TaskDTO updateTask(int taskId, TaskDTO newTaskDTO);

}
