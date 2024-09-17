package ru.tatarinov.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import org.hibernate.Hibernate;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.tatarinov.taskmanager.DTO.CommentDTO;
import ru.tatarinov.taskmanager.DTO.LoginDTO;
import ru.tatarinov.taskmanager.DTO.TaskDTOResponse;
import ru.tatarinov.taskmanager.service.CommentService;
import ru.tatarinov.taskmanager.service.TaskService;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_CLASS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts={"classpath:before_tests.sql"}, executionPhase = BEFORE_TEST_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CommentControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private TaskService taskService;
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
    @Order(10)
    void createCommentTest() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setTaskId(1);
        commentDTO.setText("test comment");

        Assert.assertEquals(false, commentService.getComment(1).isPresent());

        MvcResult mvcResult = this.mockMvc
                .perform(
                        post("/api/comment/add-comment")
                                .header("Authorization", this.token)
                                .content(objectMapper.writeValueAsString(commentDTO))
                                .contentType("application/JSON")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        TaskDTOResponse taskDTOResponse = taskService.getTaskDTOById(1);
        Hibernate.initialize(taskDTOResponse);
        Assert.assertEquals(true, commentService.getComment(1).isPresent());
    }
}