package ru.tatarinov.taskmanager.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "state")
    private TaskState taskState;

    @Column(name = "priority")
    private TaskPriority taskPriority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private User executor;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> commentList;

    public Task() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public TaskPriority getTaskPriority() {
        return taskPriority;
    }

    public User getOwner() {
        return owner;
    }

    public User getExecutor() {
        return executor;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public void setTaskPriority(TaskPriority taskPriority) {
        this.taskPriority = taskPriority;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setExecutor(User executor) {
        this.executor = executor;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}
