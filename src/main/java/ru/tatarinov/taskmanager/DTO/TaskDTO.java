package ru.tatarinov.taskmanager.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.tatarinov.taskmanager.model.TaskPriority;
import ru.tatarinov.taskmanager.model.TaskState;

public class TaskDTO {
    @NotNull(message = "field is required")
    @Schema(name = "title", example = "task-1", required = true)
    private String title;
    @Schema(name = "description", example = "some task description", required = false)
    private String description;
    @NotNull(message = "task state")
    @Schema(name = "taskState", example = "IN_PROGRESS", required = true)
    private TaskState taskState;
    @NotNull(message = "field is required")
    @Schema(name = "taskPriority", example = "HI", required = true)
    private TaskPriority taskPriority;
    @NotNull(message = "field is required")
    @Schema(name = "ownerId", example = "1", required = true)
    private Integer ownerId;

    @Schema(name = "executorId", example = "1", required = false)
    private Integer executorId;

    public TaskDTO() {
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public Integer getExecutorId() {
        return executorId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public void setExecutorId(Integer executorId) {
        this.executorId = executorId;
    }
}
