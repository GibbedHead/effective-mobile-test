package com.chaplygin.task_manager.task.dto;

import java.util.List;

public record TasksListResponseDtoPaged(
        List<TaskResponseDtoFull> tasks,
        int page,
        int size,
        int totalPages
) {
}
