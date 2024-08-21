package com.chaplygin.task_manager.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Comment list with paging info")
public record CommentPagedListResponseDto(
        @Schema(description = "List of comments DTO", implementation = CommentResponseDtoFull.class)
        List<CommentResponseDtoFull> comments,
        @Schema(description = "Comment list page number(starts with 0)", example = "0")
        int page,
        @Schema(description = "Comments per page", example = "10")
        int size,
        @Schema(description = "Comment list total pages", example = "5")
        int totalPages
) {
}
