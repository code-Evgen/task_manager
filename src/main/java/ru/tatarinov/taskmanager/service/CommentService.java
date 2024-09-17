package ru.tatarinov.taskmanager.service;

import ru.tatarinov.taskmanager.model.Comment;

import java.util.Optional;

public interface CommentService {
    void createComment(Comment comment);
    Optional<Comment> getComment(int id);
}
