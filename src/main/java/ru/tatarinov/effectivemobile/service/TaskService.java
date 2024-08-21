package ru.tatarinov.effectivemobile.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tatarinov.effectivemobile.exception.ObjectNotFoundException;
import ru.tatarinov.effectivemobile.model.Task;
import ru.tatarinov.effectivemobile.model.TaskState;
import ru.tatarinov.effectivemobile.model.User;
import ru.tatarinov.effectivemobile.repository.TaskRepository;
import ru.tatarinov.effectivemobile.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository, EntityManager entityManager) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    public void createTask(Task task){
        taskRepository.save(task);
    }

    public Optional<Task> getTaskById(int id){
        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE t.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, id).getResultList();
        if (taskList.size() == 0)
            return Optional.empty();
        return Optional.ofNullable(taskList.get(0));
    }

    public List<Task> getTaskListByOwnerId(int ownerId){
        Optional<User> ownerOptional = userRepository.findById(ownerId);
        if (ownerOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE o.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, ownerId).getResultList();
        return taskList;
    }

    public List<Task> getTaskListByOwnerId(int ownerId, int page, int tasksPerPage){
        Optional<User> ownerOptional = userRepository.findById(ownerId);
        if (ownerOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE o.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, ownerId)
                .setMaxResults(tasksPerPage)
                .setFirstResult(page * tasksPerPage)
                .getResultList();
        return taskList;
    }

    public List<Task> getTaskListByExecutorId(int executorId){
        Optional<User> executorOptional = userRepository.findById(executorId);
        if (executorOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");


        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE e.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, executorId).getResultList();
        return taskList;
    }

    public List<Task> getTaskListByExecutorId(int executorId, int page, int tasksPerPage){
        Optional<User> executorOptional = userRepository.findById(executorId);
        if (executorOptional.isEmpty())
            throw new ObjectNotFoundException("User not found");

        TypedQuery<Task> query = entityManager.createQuery("select t from Task t left join fetch t.owner o left join fetch t.executor e left join fetch t.commentList c WHERE e.id = ?1", Task.class);
        List<Task> taskList = query.setParameter(1, executorId)
                .setMaxResults(tasksPerPage)
                .setFirstResult(page * tasksPerPage)
                .getResultList();
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
    public Task updateTask(int taskId, Task newTask) {
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
        return task;
    }
}