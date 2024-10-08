package com.chaplygin.task_manager.comment.repository;

import com.chaplygin.task_manager.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTaskId(Long taskId, Pageable pageable);
}
