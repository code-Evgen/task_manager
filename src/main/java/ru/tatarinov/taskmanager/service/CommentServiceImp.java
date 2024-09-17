package ru.tatarinov.taskmanager.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.taskmanager.DTO.CommentDTO;
import ru.tatarinov.taskmanager.DTO.CommentDTOToCommentConverter;
import ru.tatarinov.taskmanager.model.Comment;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.repository.CommentRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CommentServiceImp implements CommentService{
    private final CommentRepository commentRepository;
    private final CommentDTOToCommentConverter commentDTOToCommentConverter;
    private final TaskService taskService;

    public CommentServiceImp(CommentRepository commentRepository, CommentDTOToCommentConverter commentDTOToCommentConverter, TaskService taskService) {
        this.commentRepository = commentRepository;
        this.commentDTOToCommentConverter = commentDTOToCommentConverter;
        this.taskService = taskService;
    }

    @Transactional
    public void createComment(int taskId, CommentDTO commentDTO, User user){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(commentDTOToCommentConverter);
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        Task task = taskService.getTaskById(taskId);
        comment.setTask(task);

        comment.setCommentOwner(user);
        commentRepository.save(comment);
    }

    public Optional<Comment> getComment(int id){
        return commentRepository.findById(id);
    }
}
