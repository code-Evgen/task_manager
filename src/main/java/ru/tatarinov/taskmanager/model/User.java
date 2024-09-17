package ru.tatarinov.taskmanager.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.PERSIST)
    private List<Task> ownersTaskList;

    @OneToMany(mappedBy = "executor", cascade = CascadeType.PERSIST)
    private List<Task> executorsTaskList;

    @OneToMany(mappedBy = "commentOwner", cascade = CascadeType.PERSIST)
    private List<Comment> commentList;



    public User() {
    }

    public int getId() {
        return id;
    }

    //Return mail, т.к. используем mail для аутентификации
    public String getUsername() {
        return mail;
    }

    public String getMail() {
        return mail;
    }


    public String getPassword() {
        return password;
    }

    public List<Task> getOwnersTaskList() {
        return ownersTaskList;
    }

    public List<Task> getExecutorsTaskList() {
        return executorsTaskList;
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOwnersTaskList(List<Task> ownersTaskList) {
        this.ownersTaskList = ownersTaskList;
    }

    public void setExecutorsTaskList(List<Task> executorsTaskList) {
        this.executorsTaskList = executorsTaskList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

}
