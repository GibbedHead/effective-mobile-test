package com.chaplygin.task_manager.comment.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public record CommentCreateDto(
        @NotBlank String text
) implements Serializable {
}