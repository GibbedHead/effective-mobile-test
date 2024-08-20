package com.chaplygin.task_manager.task.dto;


import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;

public record TaskResponseDtoFull(
        Long id,
        String title,
        String description,
        UserResponseDtoFull owner,
        UserResponseDtoFull assignee,
        Status status,
        Priority priority
) {
}
