package com.chaplygin.task_manager.comment.mapper;

import com.chaplygin.task_manager.comment.dto.CommentPagedListResponseDto;
import com.chaplygin.task_manager.comment.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface CommentListMapper {

    @Mapping(source = "content", target = "comments")
    @Mapping(source = "number", target = "page")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "totalPages", target = "totalPages")
    CommentPagedListResponseDto pageToCommentPagedListResponseDto(Page<Comment> page);
}
