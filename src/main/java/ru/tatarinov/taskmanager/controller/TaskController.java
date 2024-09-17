package ru.tatarinov.taskmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.tatarinov.taskmanager.DTO.*;
import ru.tatarinov.taskmanager.exception.AuthorizationFailException;
import ru.tatarinov.taskmanager.model.TaskState;
import ru.tatarinov.taskmanager.service.AuthorizationService;
import ru.tatarinov.taskmanager.service.CommentService;
import ru.tatarinov.taskmanager.service.TaskService;
import ru.tatarinov.taskmanager.service.UserService;
import ru.tatarinov.taskmanager.util.BindingResultValidation;
import ru.tatarinov.taskmanager.validation.TaskValidation;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "Task manager API")
@RequestMapping(value = "/api/task")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final CommentService commentService;
    private final CommentDTOToCommentConverter commentDTOToCommentConverter;
    private final TaskValidation taskValidation;
    private final AuthorizationService authorizationService;

    public TaskController(TaskService taskService, UserService userService, CommentService commentService, CommentDTOToCommentConverter commentDTOToCommentConverter, TaskValidation taskValidation, AuthorizationService authorizationService) {
        this.taskService = taskService;
        this.userService = userService;
        this.commentService = commentService;
        this.commentDTOToCommentConverter = commentDTOToCommentConverter;
        this.taskValidation = taskValidation;
        this.authorizationService = authorizationService;
    }

    @PostMapping(consumes = "application/json")
    @Operation(summary = "Create task", description = "Create new task")
    public ResponseEntity<HttpStatus> createTask(@RequestBody() @Valid TaskDTO taskDTO, BindingResult bindingResult){
        taskValidation.validate(taskDTO, bindingResult);

        BindingResultValidation.bindingResultCheck(bindingResult);

        taskService.createTask(taskDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json", produces = "application/json")
    @Operation(summary = "Update task data", description = "Updates and returns updated task data")
    public ResponseEntity<TaskDTO> updateTask(@RequestParam("id") @Parameter(name = "id", description = "updating task id", example = "1", required = true) int taskId,
                                              @RequestBody() @Valid TaskDTO newTaskDTO, BindingResult bindingResult){
        taskValidation.validate(newTaskDTO, bindingResult);
        BindingResultValidation.bindingResultCheck(bindingResult);

        if (!authorizationService.ownerAuthorization(taskId))
            throw new AuthorizationFailException("Not enough rights to update this task");


        TaskDTO updatedTaskDTO = taskService.updateTask(taskId, newTaskDTO);

        return ResponseEntity.ok(updatedTaskDTO);
    }


    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Get task by task id", description = "Returns task")
    public ResponseEntity<TaskDTOResponse> getTask(@PathVariable("id") @Parameter(name = "id", description = "task id", example = "1") int id){

        TaskDTOResponse taskDTOResponse = taskService.getTaskDTOById(id);
        return ResponseEntity.ok(taskDTOResponse);
    }

    @GetMapping(value = "/get-by-owner", produces = "application/json")
    @Operation(summary = "Get task by owner id", description = "Returns task list for owner")
    public ResponseEntity<List<TaskDTOResponse>> getTaskListByOwner(@RequestParam("id") @Parameter(name = "id", description = "Owner id", example = "1") int ownerId,
                                                                    @RequestParam(value = "page", required = false) @Parameter(name = "page", description = "Pagination. Page number. Start from 0", example = "2", required = false) Integer page,
                                                                    @RequestParam(value = "tasks_per_page", required = false) @Parameter(name = "tasks_per_page", description = "Pagination. Tasks per page", example = "2", required = false) Integer tasksPerPage){

        List<TaskDTOResponse> taskDTOResponseList = new ArrayList<>();
        if (page == null || tasksPerPage == null)
            taskDTOResponseList = taskService.getTaskDTOListByOwnerId(ownerId);
        else
            taskDTOResponseList = taskService.getTaskDTOListByOwnerId(ownerId, page, tasksPerPage);



        return ResponseEntity.ok(taskDTOResponseList);
    }

    @GetMapping(value = "/get-by-executor", produces = "application/json")
    @Operation(summary = "Get task by executor id", description = "Returns task list for executor")
    public ResponseEntity<List<TaskDTOResponse>> getTaskListByExecutor(@RequestParam("id") @Parameter(name = "id", description = "Executor id", example = "1") int executorId,
                                                               @RequestParam(value = "page", required = false) @Parameter(name = "page", description = "Pagination. Page number. Start from 0", example = "2", required = false) Integer page,
                                                               @RequestParam(value = "tasks_per_page", required = false) @Parameter(name = "tasks_per_page", description = "Pagination. Tasks per page", example = "2", required = false) Integer tasksPerPage){

        List<TaskDTOResponse> taskDTOResponseList = new ArrayList<>();
        if (page == null || tasksPerPage == null)
            taskDTOResponseList = taskService.getTaskDTOListByExecutorId(executorId);
        else
            taskDTOResponseList = taskService.getTaskDTOListByExecutorId(executorId, page, tasksPerPage);


        return ResponseEntity.ok(taskDTOResponseList);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task", description = "Delete task by id")
    public ResponseEntity<HttpStatus> deleteTask (@PathVariable("id") @Parameter(name = "id", description = "Deleting task id", example = "1") int taskId){
        if (!authorizationService.ownerAuthorization(taskId))
            throw new AuthorizationFailException("Not enough rights to delete this task");

        taskService.deleteTask(taskId);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/set-status")
    @Operation(summary = "Change task status", description = "Changing task status by task id")
    public ResponseEntity<HttpStatus> changeTaskStatus (@RequestParam("id") @Parameter(name = "id", description = "Task id", example = "1") int taskId,
                                                        @RequestParam("task-state") TaskState taskState){
        if (!authorizationService.ownerAuthorization(taskId))
            throw new AuthorizationFailException("Not enough rights to update state of this task");

        taskService.changeTaskStatus(taskId, taskState);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/set-executor")
    @Operation(summary = "Set executor", description = "Set executor id for task")
    public ResponseEntity<HttpStatus> setExecutor (@RequestParam("id") @Parameter(name = "id", description = "Task id", example = "1") int taskId,
                                                   @RequestParam("executor-id") @Parameter(name = "executor-id", description = "Executor id for task", example = "1") int executorId){
        if (!authorizationService.ownerAuthorization(taskId))
            throw new AuthorizationFailException("Not enough rights to set executor for this task");

        taskService.setExecutor(taskId, executorId);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
