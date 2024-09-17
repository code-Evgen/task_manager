package ru.tatarinov.taskmanager.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.taskmanager.DTO.TaskDTO;
import ru.tatarinov.taskmanager.DTO.TaskDTOResponse;
import ru.tatarinov.taskmanager.DTO.TaskDTOToTaskConverter;
import ru.tatarinov.taskmanager.DTO.TaskToTaskDTOResponseConverter;
import ru.tatarinov.taskmanager.exception.ObjectNotFoundException;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.model.TaskState;
import ru.tatarinov.taskmanager.model.User;
import ru.tatarinov.taskmanager.repository.TaskRepository;
import ru.tatarinov.taskmanager.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TaskServiceImp implements TaskService{
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final TaskDTOToTaskConverter taskDTOToTaskConverter;
    private final TaskToTaskDTOResponseConverter taskToTaskDTOResponseConverter;

    public TaskServiceImp(TaskRepository taskRepository, UserRepository userRepository, EntityManager entityManager, TaskDTOToTaskConverter taskDTOToTaskConverter, TaskToTaskDTOResponseConverter taskToTaskDTOResponseConverter) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
        this.taskDTOToTaskConverter = taskDTOToTaskConverter;
        this.taskToTaskDTOResponseConverter = taskToTaskDTOResponseConverter;
    }

    @Transactional
    public void createTask(TaskDTO taskDTO){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskDTOToTaskConverter);
        Task task = modelMapper.map(taskDTO, Task.class);
        taskRepository.save(task);
    }

    public Task getTaskById(int id){
        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE t.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, id).getResultList();
        if (taskList.size() == 0)
            throw new ObjectNotFoundException("Task not found");

        Task task = taskList.get(0);

        return task;
    }

    public TaskDTOResponse getTaskDTOById(int id){
        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE t.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, id).getResultList();
        if (taskList.size() == 0)
            throw new ObjectNotFoundException("Task not found");

        Task task = taskList.get(0);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskToTaskDTOResponseConverter);
        TaskDTOResponse taskDTOResponse = modelMapper.map(task, TaskDTOResponse.class);

        return taskDTOResponse;
    }

    public List<TaskDTOResponse> getTaskDTOListByOwnerId(int ownerId){
        Optional<User> ownerOptional = userRepository.findById(ownerId);
        if (ownerOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE o.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, ownerId).getResultList();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskToTaskDTOResponseConverter);
        List<TaskDTOResponse> taskDTOResponseList = taskList.stream().map(x -> modelMapper.map(x, TaskDTOResponse.class)).collect(Collectors.toList());

        return taskDTOResponseList;
    }

    public List<Task> getTaskListByOwnerId(int ownerId){
        Optional<User> ownerOptional = userRepository.findById(ownerId);
        if (ownerOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE o.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, ownerId).getResultList();
        return taskList;
    }

    public List<TaskDTOResponse> getTaskDTOListByOwnerId(int ownerId, int page, int tasksPerPage){
        Optional<User> ownerOptional = userRepository.findById(ownerId);
        if (ownerOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE o.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, ownerId)
                .setMaxResults(tasksPerPage)
                .setFirstResult(page * tasksPerPage)
                .getResultList();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskToTaskDTOResponseConverter);
        List<TaskDTOResponse> taskDTOResponseList = taskList.stream().map(x -> modelMapper.map(x, TaskDTOResponse.class)).collect(Collectors.toList());

        return taskDTOResponseList;
    }

    public List<TaskDTOResponse> getTaskDTOListByExecutorId(int executorId){
        Optional<User> executorOptional = userRepository.findById(executorId);
        if (executorOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");


        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE e.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, executorId).getResultList();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskToTaskDTOResponseConverter);
        List<TaskDTOResponse> taskDTOList = taskList.stream().map(x -> modelMapper.map(x, TaskDTOResponse.class)).collect(Collectors.toList());


        return taskDTOList;
    }

    public List<TaskDTOResponse> getTaskDTOListByExecutorId(int executorId, int page, int tasksPerPage){
        Optional<User> executorOptional = userRepository.findById(executorId);
        if (executorOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE e.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, executorId)
                .setMaxResults(tasksPerPage)
                .setFirstResult(page * tasksPerPage)
                .getResultList();

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskToTaskDTOResponseConverter);
        List<TaskDTOResponse> taskDTOList = taskList.stream().map(x -> modelMapper.map(x, TaskDTOResponse.class)).collect(Collectors.toList());


        return taskDTOList;
    }

    public List<Task> getTaskListByExecutorId(int executorId){
        Optional<User> executorOptional = userRepository.findById(executorId);
        if (executorOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE e.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, executorId).getResultList();

        return taskList;
    }

    @Transactional
    public void deleteTask(int taskId){
        taskRepository.deleteById(taskId);
    }

    @Transactional
    public boolean changeTaskStatus(int taskId, TaskState newTaskState) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty())
            throw new ObjectNotFoundException("Task not found");

        Task task = taskOptional.get();
        task.setTaskState(newTaskState);
        taskRepository.save(task);
        return true;
    }

    @Transactional
    public boolean setExecutor(int taskId, int executorId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty())
            throw new ObjectNotFoundException("Task not found");
        Optional<User> userOptional = userRepository.findById(executorId);
        if (userOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        Task task = taskOptional.get();
        User executor = userOptional.get();
        task.setExecutor(executor);
        taskRepository.save(task);
        return true;
    }

    @Transactional
    public TaskDTO updateTask(int taskId, TaskDTO newTaskDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(taskDTOToTaskConverter);
        Task newTask = modelMapper.map(newTaskDTO, Task.class);

        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isEmpty())
            throw new ObjectNotFoundException("Task not found");

        Task task = taskOptional.get();
        task.setTitle(newTask.getTitle());
        task.setDescription(newTask.getDescription());
        task.setTaskPriority(newTask.getTaskPriority());
        task.setTaskState(newTask.getTaskState());
        task.setOwner(newTask.getOwner());
        task.setExecutor(newTask.getExecutor());

        ModelMapper modelMapperResult = new ModelMapper();
        modelMapperResult.addConverter(taskToTaskDTOResponseConverter);
        TaskDTO resultTaskDTO = modelMapper.map(task, TaskDTO.class);

        return resultTaskDTO;
    }
}