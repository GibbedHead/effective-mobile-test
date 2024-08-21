package com.chaplygin.task_manager.task.dto;

import java.util.List;

public record TaskPagedListResponseDto(
        List<TaskResponseDtoNoComments> tasks,
        int page,
        int size,
        int totalPages
) {
}
