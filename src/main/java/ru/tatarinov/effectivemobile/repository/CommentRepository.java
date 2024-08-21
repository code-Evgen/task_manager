package ru.tatarinov.effectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tatarinov.effectivemobile.model.Comment;
import ru.tatarinov.effectivemobile.model.Task;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
