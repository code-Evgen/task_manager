package ru.tatarinov.taskmanager.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.tatarinov.taskmanager.DTO.LoginDTO;
import ru.tatarinov.taskmanager.DTO.TaskDTO;
import ru.tatarinov.taskmanager.DTO.TaskDTOResponse;
import ru.tatarinov.taskmanager.exception.ObjectNotFoundException;
import ru.tatarinov.taskmanager.model.TaskPriority;
import ru.tatarinov.taskmanager.model.TaskState;
import ru.tatarinov.taskmanager.service.CommentServiceImp;
import ru.tatarinov.taskmanager.service.TaskService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts={"classpath:before_tests.sql"}, executionPhase = BEFORE_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private CommentServiceImp commentService;
    private MockMvc mockMvc;
    private String token;


    @BeforeEach
    void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    @BeforeEach
    void setupJWT() throws Exception {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setMail("base@mail.ru");
        loginDTO.setPassword("password");

        MvcResult mvcResult = this.mockMvc
                .perform(
                        post("/login")
                                .content(objectMapper.writeValueAsString(loginDTO))
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andReturn();
        String resultContent = mvcResult.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(resultContent);
        this.token = "Bearer " + jsonObject.getString("token");
        System.out.println(token);
    }

    @Test
    @Order(5)
    void createTaskTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("create test");
        taskDTO.setDescription("test task");
        taskDTO.setTaskState(TaskState.WAITING);
        taskDTO.setTaskPriority(TaskPriority.LOW);
        taskDTO.setOwnerId(1);
        taskDTO.setExecutorId(1);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        post("/api/task")
                                .header("Authorization", this.token)
                                .content(objectMapper.writeValueAsString(taskDTO))
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("create test", taskService.getTaskDTOById(4).getTitle());
    }

    @Test
    @Order(5)
    void getTask() throws Exception {
        TaskDTOResponse taskDTOResponse = new TaskDTOResponse();
        taskDTOResponse.setTitle("task_base");
        taskDTOResponse.setDescription("test base task");
        taskDTOResponse.setOwnerId(1);


        MvcResult mvcResult = this.mockMvc
                .perform(
                        get("/api/task/1")
                                .header("Authorization", this.token)
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/JSON"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        TaskDTOResponse response = objectMapper.readValue(content, new TypeReference<TaskDTOResponse>() {});
        Assert.assertEquals("task_base", response.getTitle());
    }

    @Test
    @Order(10)
    void updateTaskTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("check_update");
        taskDTO.setDescription("test task update");
        taskDTO.setTaskState(TaskState.COMPLETED);
        taskDTO.setTaskPriority(TaskPriority.HI);
        taskDTO.setOwnerId(1);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        put("/api/task")
                                .header("Authorization", this.token)
                                .param("id", "1")
                                .content(objectMapper.writeValueAsString(taskDTO))
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(taskDTO)))
                .andReturn();

    }

    @Test
    @Order(10)
    void getTaskListByOwnerTest() throws Exception {

        List<TaskDTOResponse> taskDTOResponseList = new ArrayList<>();
        TaskDTOResponse taskDTOResponse = new TaskDTOResponse();
        taskDTOResponse.setTitle("task1");
        taskDTOResponse.setDescription("test task1");
        taskDTOResponse.setTaskState(TaskState.WAITING);
        taskDTOResponse.setTaskPriority(TaskPriority.MEDIUM);
        taskDTOResponse.setOwnerId(2);
        taskDTOResponse.setExecutorId(3);


        taskDTOResponseList.add(taskDTOResponse);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        get("/api/task/get-by-owner")
                                .header("Authorization", this.token)
                                .param("id", "2")
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/JSON"))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        List<TaskDTOResponse> responseList = objectMapper.readValue(content, new TypeReference<List<TaskDTOResponse>>() {});
        Assert.assertEquals("task1", responseList.get(0).getTitle());
    }

    @Test
    @Order(10)
    void getTaskListByExecutorTest() throws Exception {
        List<TaskDTOResponse> taskDTOResponseList = new ArrayList<>();

        TaskDTOResponse taskDTOResponse2 = new TaskDTOResponse();
        taskDTOResponse2.setTitle("task2");
        taskDTOResponse2.setDescription("test task2");
        taskDTOResponse2.setTaskState(TaskState.WAITING);
        taskDTOResponse2.setTaskPriority(TaskPriority.MEDIUM);
        taskDTOResponse2.setOwnerId(3);
        taskDTOResponse2.setExecutorId(2);
        taskDTOResponse2.setCommentList(null);

        taskDTOResponseList.add(taskDTOResponse2);

        MvcResult mvcResult = this.mockMvc
                .perform(
                        get("/api/task/get-by-executor")
                                .header("Authorization", this.token)
                                .param("id", "2")
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/JSON"))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        List<TaskDTOResponse> responseList = objectMapper.readValue(content, new TypeReference<List<TaskDTOResponse>>() {});
        Assert.assertEquals("task2", responseList.get(0).getTitle());
    }


    @Test
    @Order(10)
    void changeTaskStatusTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        patch("/api/task/set-status")
                                .header("Authorization", this.token)
                                .param("id", "1")
                                .param("task-state", "COMPLETED")
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        TaskDTOResponse taskDTOResponse = taskService.getTaskDTOById(1);
        Assert.assertEquals(TaskState.COMPLETED, taskDTOResponse.getTaskState());
    }

    @Test
    @Order(10)
    void setExecutorTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        patch("/api/task/set-executor")
                                .header("Authorization", this.token)
                                .param("id", "1")
                                .param("executor-id", "1")
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        TaskDTOResponse taskDTOResponse = taskService.getTaskDTOById(1);
        Assert.assertEquals(1, (int)taskDTOResponse.getExecutorId());
    }

    @Test
    @Order(100)
    void deleteTaskTest() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(
                        MockMvcRequestBuilders.delete("/api/task/1")
                                .header("Authorization", this.token)
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertThrows(ObjectNotFoundException.class, () -> taskService.getTaskDTOById(1));
    }
}