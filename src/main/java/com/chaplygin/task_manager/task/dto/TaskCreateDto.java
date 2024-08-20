package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Priority;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record TaskCreateDto(
        @NotNull @Size(max = 255) String title,
        @NotNull String description,
        @NotNull Long assigneeId,
        @NotNull Priority priority
) implements Serializable {
}