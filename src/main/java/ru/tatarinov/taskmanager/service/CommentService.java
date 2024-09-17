package ru.tatarinov.taskmanager.service;

import ru.tatarinov.taskmanager.DTO.CommentDTO;
import ru.tatarinov.taskmanager.model.Comment;
import ru.tatarinov.taskmanager.model.User;

import java.util.Optional;

public interface CommentService {
    void createComment(int taskId, CommentDTO commentDTO, User user);
    Optional<Comment> getComment(int id);
}
