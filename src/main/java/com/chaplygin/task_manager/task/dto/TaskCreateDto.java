package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Schema(description = "Data Transfer Object for creating a new task.")
public record TaskCreateDto(
        @Schema(description = "Title of the task. Must not be blank and should not exceed 255 characters.",
                example = "Implement new feature",
                maxLength = 255)
        @NotBlank @Size(max = 255) String title,

        @Schema(description = "Description of the task. Must not be blank.",
                example = "Implement a new feature to enhance the user experience.")
        @NotBlank String description,

        @Schema(description = "ID of the user assigned to the task. Must be a positive number.",
                example = "456")
        @NotNull @Positive Long assigneeId,

        @Schema(description = "Priority of the task. Cannot be null.",
                example = "HIGH")
        @NotNull Priority priority
) implements Serializable {
}