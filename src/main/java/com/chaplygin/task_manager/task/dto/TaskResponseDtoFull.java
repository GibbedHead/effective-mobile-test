package com.chaplygin.task_manager.task.dto;


import com.chaplygin.task_manager.comment.dto.CommentResponseDtoFull;
import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;

import java.util.List;

public record TaskResponseDtoFull(
        Long id,
        String title,
        String description,
        UserResponseDtoFull owner,
        UserResponseDtoFull assignee,
        Status status,
        Priority priority,
        List<CommentResponseDtoFull> comments
) {
}
