package com.chaplygin.task_manager.comment.dto;

import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Comment response with author")
public record CommentResponseDtoFull(
        @Schema(description = "Comment text", example = "Very cool task!")
        String text,
        @Schema(implementation = UserResponseDtoFull.class)
        UserResponseDtoFull author
) implements Serializable {
}