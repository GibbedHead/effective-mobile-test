package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record TaskFullUpdateDto(
        @Size(max = 255) String title,
        String description,
        Long assigneeId,
        Status status,
        Priority priority
) implements Serializable {
}
