package ru.tatarinov.effectivemobile.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User commentOwner;

    @ManyToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;


    public Comment() {
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public User getCommentOwner() {
        return commentOwner;
    }

    public Task getTask() {
        return task;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCommentOwner(User commentOwner) {
        this.commentOwner = commentOwner;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}
