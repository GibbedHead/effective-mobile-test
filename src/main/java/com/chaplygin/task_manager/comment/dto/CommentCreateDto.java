package com.chaplygin.task_manager.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Schema(description = "Comment create request")
public record CommentCreateDto(
        @Schema(description = "Comment text", example = "Very cool task!")
        @NotBlank String text
) implements Serializable {
}