package com.chaplygin.task_manager.task.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data Transfer Object representing a paginated list of tasks without comments.")
public record TaskPagedListResponseDto(
        @Schema(description = "List of tasks on the current page. Each task does not include comments.",
                implementation = TaskResponseDtoNoComments.class)
        List<TaskResponseDtoNoComments> tasks,

        @Schema(description = "Current page number in the paginated list. Starts from 1.",
                example = "1")
        int page,

        @Schema(description = "Number of tasks per page.",
                example = "10")
        int size,

        @Schema(description = "Total number of pages available based on the current page size.",
                example = "5")
        int totalPages
) {
}
