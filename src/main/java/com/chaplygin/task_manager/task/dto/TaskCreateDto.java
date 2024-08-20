package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record TaskCreateDto(
        @NotBlank @Size(max = 255) String title,
        @NotBlank String description,
        @NotNull @Positive Long assigneeId,
        @NotBlank @Size(max = 50) Priority priority
) implements Serializable {
}