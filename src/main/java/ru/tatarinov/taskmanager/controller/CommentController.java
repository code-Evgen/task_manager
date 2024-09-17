package ru.tatarinov.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tatarinov.taskmanager.DTO.CommentDTO;
import ru.tatarinov.taskmanager.DTO.CommentDTOToCommentConverter;
import ru.tatarinov.taskmanager.exception.AuthorizationFailException;
import ru.tatarinov.taskmanager.model.Comment;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.service.AuthorizationService;
import ru.tatarinov.taskmanager.service.CommentService;
import ru.tatarinov.taskmanager.util.BindingResultValidation;

@RestController
@Tag(name = "Comment manager API")
@RequestMapping(value = "/api/comment")
public class CommentController {
    private final AuthorizationService authorizationService;
    private final CommentDTOToCommentConverter commentDTOToCommentConverter;
    private final CommentService commentService;

    public CommentController(AuthorizationService authorizationService, CommentDTOToCommentConverter commentDTOToCommentConverter, CommentService commentService) {
        this.authorizationService = authorizationService;
        this.commentDTOToCommentConverter = commentDTOToCommentConverter;
        this.commentService = commentService;
    }


    @PostMapping(value = "/add-comment", consumes = "application/json")
    @Operation(summary = "Create comment", description = "Create comment for task by task id")
    public ResponseEntity<HttpStatus> createComment(@RequestBody() @Valid CommentDTO commentDTO, BindingResult bindingResult){
        BindingResultValidation.bindingResultCheck(bindingResult);

        Integer taskId = commentDTO.getTaskId();
        if (!(authorizationService.ownerAuthorization(taskId) || authorizationService.executorAuthorization(taskId)))
            throw new AuthorizationFailException("Not enough rights to add comment to this task");


        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(commentDTOToCommentConverter);
        Comment comment = modelMapper.map(commentDTO, Comment.class);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setCommentOwner(user);

        commentService.createComment(comment);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
