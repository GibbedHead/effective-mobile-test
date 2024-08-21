package com.chaplygin.task_manager.task.dto;


import com.chaplygin.task_manager.comment.dto.CommentResponseDtoFull;
import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Data Transfer Object representing the full details of a task, including comments.")
public record TaskResponseDtoFull(
        @Schema(description = "Unique identifier of the task.",
                example = "1")
        Long id,

        @Schema(description = "Title of the task.",
                example = "Complete project documentation")
        String title,

        @Schema(description = "Description of the task.",
                example = "Prepare and finalize the project documentation for the upcoming release.")
        String description,

        @Schema(description = "Owner of the task, who originally created it.",
                implementation = UserResponseDtoFull.class)
        UserResponseDtoFull owner,

        @Schema(description = "User currently assigned to the task.",
                implementation = UserResponseDtoFull.class)
        UserResponseDtoFull assignee,

        @Schema(description = "Current status of the task.",
                example = "IN_PROGRESS")
        Status status,

        @Schema(description = "Priority level of the task.",
                example = "HIGH")
        Priority priority,

        @Schema(description = "List of comments associated with the task.",
                implementation = CommentResponseDtoFull.class)
        List<CommentResponseDtoFull> comments
) {
}
