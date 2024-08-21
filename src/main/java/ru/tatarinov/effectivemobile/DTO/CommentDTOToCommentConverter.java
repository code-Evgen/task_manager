package ru.tatarinov.effectivemobile.DTO;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import ru.tatarinov.effectivemobile.exception.ObjectNotFoundException;
import ru.tatarinov.effectivemobile.model.Comment;
import ru.tatarinov.effectivemobile.model.Task;
import ru.tatarinov.effectivemobile.model.User;
import ru.tatarinov.effectivemobile.service.TaskService;
import ru.tatarinov.effectivemobile.service.UserService;

import java.util.Optional;

@Component
public class CommentDTOToCommentConverter implements Converter<CommentDTO, Comment> {
    private final UserService userService;
    private final TaskService taskService;

    public CommentDTOToCommentConverter(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @Override
    public Comment convert(MappingContext<CommentDTO, Comment> mappingContext) {
        Comment comment = mappingContext.getDestination();
        CommentDTO commentDTO = mappingContext.getSource();
        if (comment == null) {
            comment = new Comment();
        }
        comment.setText(commentDTO.getText());


        if (commentDTO.getTaskId() != null) {
            Optional<Task> taskOptional = taskService.getTaskById(commentDTO.getTaskId());
            if (taskOptional.isEmpty())
                throw new ObjectNotFoundException("Task not found");
            Task task = taskOptional.get();
            comment.setTask(task);
        }
        else
            comment.setTask(null);

        return comment;
    }
}
