package ru.tatarinov.taskmanager.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.taskmanager.model.Comment;
import ru.tatarinov.taskmanager.repository.CommentRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService{
    private final CommentRepository commentRepository;

    public CommentServiceImp(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void createComment(Comment comment){
        commentRepository.save(comment);
    }

    public Optional<Comment> getComment(int id){
        return commentRepository.findById(id);
    }
}
