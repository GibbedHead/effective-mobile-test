package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Data Transfer Object for updating the status of a task.")
public record TaskStatusUpdateDto(
        @Schema(description = "New status of the task.",
                example = "IN_PROGRESS")
        Status status
) implements Serializable {
}
