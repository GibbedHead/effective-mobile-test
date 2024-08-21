package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Schema(description = "Data Transfer Object for fully updating a task.")
public record TaskFullUpdateDto(
        @Schema(description = "New title of the task. Can be null, but if provided, must not exceed 255 characters.",
                example = "Update task management system",
                maxLength = 255)
        @Size(max = 255)
        String title,

        @Schema(description = "New description of the task. Can be null.",
                example = "Refactor the existing codebase for better performance.")
        String description,

        @Schema(description = "ID of the user assigned to the task. Can be null.",
                example = "123")
        Long assigneeId,

        @Schema(description = "New status of the task. Can be null.",
                example = "IN_PROGRESS")
        Status status,

        @Schema(description = "New priority of the task. Can be null.",
                example = "MEDIUM")
        Priority priority
) implements Serializable {
}
