package com.chaplygin.task_manager.comment.repository;

import com.chaplygin.task_manager.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
