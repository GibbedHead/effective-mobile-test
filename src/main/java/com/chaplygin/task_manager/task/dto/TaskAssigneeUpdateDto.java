package com.chaplygin.task_manager.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Data Transfer Object for updating the assignee of a task.")
public record TaskAssigneeUpdateDto(
        @Schema(description = "ID of the user to be assigned to the task", example = "123")
        Long assigneeId
) implements Serializable {
}
