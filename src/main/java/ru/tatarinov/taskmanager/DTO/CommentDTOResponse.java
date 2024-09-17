package ru.tatarinov.taskmanager.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

public class CommentDTOResponse {
    @Schema(name = "id", example = "1")
    private int id;

    @Schema(name = "text", example = "new comment for task")
    private String text;

    @Schema(name = "commentOwnerId", example = "1")
    private Integer commentOwnerId;

    public CommentDTOResponse() {
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getCommentOwnerId() {
        return commentOwnerId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCommentOwnerId(Integer commentOwnerId) {
        this.commentOwnerId = commentOwnerId;
    }
}
