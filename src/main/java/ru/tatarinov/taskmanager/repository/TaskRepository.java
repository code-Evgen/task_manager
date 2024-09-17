package ru.tatarinov.taskmanager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.model.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> getTasksByOwner(User owner);
    List<Task> getTasksByOwner(User owner, Pageable pageable);
    List<Task> getTasksByExecutor(User executor);
    List<Task> getTasksByExecutor(User executor, Pageable pageable);

}
