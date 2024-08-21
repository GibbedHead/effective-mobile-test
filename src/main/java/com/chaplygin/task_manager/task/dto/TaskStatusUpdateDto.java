package com.chaplygin.task_manager.task.dto;

import com.chaplygin.task_manager.task.model.Status;

import java.io.Serializable;

public record TaskStatusUpdateDto(
        Status status
) implements Serializable {
}
