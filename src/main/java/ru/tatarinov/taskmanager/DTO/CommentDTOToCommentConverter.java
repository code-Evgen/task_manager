package ru.tatarinov.taskmanager.DTO;


import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import ru.tatarinov.taskmanager.model.Comment;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.service.TaskService;
import ru.tatarinov.taskmanager.service.UserService;

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
            Task task = taskService.getTaskById(commentDTO.getTaskId());
            comment.setTask(task);
        }
        else
            comment.setTask(null);

        return comment;
    }
}
