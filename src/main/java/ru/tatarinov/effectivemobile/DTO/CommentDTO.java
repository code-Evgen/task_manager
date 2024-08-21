package ru.tatarinov.effectivemobile.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import ru.tatarinov.effectivemobile.model.Task;
import ru.tatarinov.effectivemobile.model.User;

public class CommentDTO {
    @NotNull(message = "field is required")
    @Schema(name = "text", example = "new comment for task", required = true)
    private String text;

    @NotNull(message = "field is required")
    @Schema(name = "taskId", example = "1", required = true)
    private Integer taskId;

    public CommentDTO() {
    }

    public String getText() {
        return text;
    }


    public Integer getTaskId() {
        return taskId;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
