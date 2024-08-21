package com.chaplygin.task_manager.comment.dto;

import java.util.List;

public record CommentPagedListResponseDto(
        List<CommentResponseDtoFull> comments,
        int page,
        int size,
        int totalPages
) {
}
