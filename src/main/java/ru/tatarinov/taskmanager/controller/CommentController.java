package ru.tatarinov.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.taskmanager.DTO.CommentDTO;
import ru.tatarinov.taskmanager.DTO.CommentDTOToCommentConverter;
import ru.tatarinov.taskmanager.aop.Authorization;
import ru.tatarinov.taskmanager.aop.AuthorizationType;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.service.AuthorizationServiceImp;
import ru.tatarinov.taskmanager.service.CommentServiceImp;
import ru.tatarinov.taskmanager.util.BindingResultValidation;

@RestController
@Tag(name = "Comment manager API")
@RequestMapping(value = "/api/comment")
public class CommentController {
    private final AuthorizationServiceImp authorizationService;
    private final CommentDTOToCommentConverter commentDTOToCommentConverter;
    private final CommentServiceImp commentService;

    public CommentController(AuthorizationServiceImp authorizationService, CommentDTOToCommentConverter commentDTOToCommentConverter, CommentServiceImp commentService) {
        this.authorizationService = authorizationService;
        this.commentDTOToCommentConverter = commentDTOToCommentConverter;
        this.commentService = commentService;
    }


    @PostMapping(value = "/add-comment", consumes = "application/json")
    @Operation(summary = "Create comment", description = "Create comment for task by task id")
    @Authorization(type = AuthorizationType.BOTH)
    public ResponseEntity<HttpStatus> createComment(@RequestParam("id") @Parameter(name = "id", description = "Task id", example = "1") int taskId,
                                                    @RequestBody() @Valid CommentDTO commentDTO, BindingResult bindingResult){
        BindingResultValidation.bindingResultCheck(bindingResult);

//        Integer taskId = commentDTO.getTaskId();
//        if (!(authorizationService.ownerAuthorization(taskId) || authorizationService.executorAuthorization(taskId)))
//            throw new AuthorizationFailException("Not enough rights to add comment to this task");

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        commentService.createComment(taskId, commentDTO, user);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
