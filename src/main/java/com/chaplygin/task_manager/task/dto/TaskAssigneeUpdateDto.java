package com.chaplygin.task_manager.task.dto;

import java.io.Serializable;

public record TaskAssigneeUpdateDto(
        Long assigneeId
) implements Serializable {
}
