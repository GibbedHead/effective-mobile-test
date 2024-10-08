package com.chaplygin.task_manager.comment.service;

import com.chaplygin.task_manager.comment.model.Comment;
import com.chaplygin.task_manager.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional
    @CacheEvict(value = "comments", key = "#comment.task.id")
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    @Cacheable(value = "comments", key = "#taskId")
    public Page<Comment> getCommentsByTaskId(Long taskId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        return commentRepository.findByTaskId(taskId, pageable);
    }
}
