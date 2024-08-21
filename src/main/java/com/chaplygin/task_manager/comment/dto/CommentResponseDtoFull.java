package com.chaplygin.task_manager.comment.dto;

import com.chaplygin.task_manager.user.dto.UserResponseDtoFull;

import java.io.Serializable;

public record CommentResponseDtoFull(
        String text,
        UserResponseDtoFull author
) implements Serializable {
}