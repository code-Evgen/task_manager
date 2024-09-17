package ru.tatarinov.taskmanager.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public class CommentDTO {
    @NotNull(message = "field is required")
    @Schema(name = "text", example = "new comment for task", required = true)
    private String text;

//    @NotNull(message = "field is required")
//    @Schema(name = "taskId", example = "1", required = true)
//    private Integer taskId;

    public CommentDTO() {
    }

    public String getText() {
        return text;
    }


//    public Integer getTaskId() {
//        return taskId;
//    }

    public void setText(String text) {
        this.text = text;
    }


//    public void setTaskId(Integer taskId) {
//        this.taskId = taskId;
//    }
}
