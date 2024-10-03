package ru.tatarinov.taskmanager.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.tatarinov.taskmanager.model.Task;
import ru.tatarinov.taskmanager.model.TaskPriority;
import ru.tatarinov.taskmanager.model.TaskState;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts={"classpath:before_tests.sql"}, executionPhase = BEFORE_TEST_CLASS)
class TaskServiceImpCacheTest {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TaskServiceImp taskServiceImp;

    @SpyBean
    @Autowired
    private EntityManager entityManager;;


    @Test
    void createTaskCache() {
        Task task = new Task();
        task.setTaskState(TaskState.WAITING);
        task.setTaskPriority(TaskPriority.LOW);
        task.setTitle("test");

        taskServiceImp.createTask(task);

        final int id = 4;
        Task cachedTask = taskServiceImp.getTaskById(4);
        assertNotNull(task, "task not found");

        verify(entityManager, never()).createQuery(anyString(), eq(Task.class));

    }

    @Test
    void getTaskByIdCache() {
        final int id = 1;

        Task task = taskServiceImp.getTaskById(id);
        assertNotNull(task, "task not found");

        Task cachedTask = taskServiceImp.getTaskById(id);
        assertNotNull(task, "task not found");

        verify(entityManager, times(1)).createQuery(anyString(), eq(Task.class));

    }
}