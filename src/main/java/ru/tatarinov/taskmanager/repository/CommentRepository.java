package ru.tatarinov.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.taskmanager.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
