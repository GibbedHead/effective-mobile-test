package com.chaplygin.task_manager.comment.mapper;

import com.chaplygin.task_manager.comment.dto.CommentCreateDto;
import com.chaplygin.task_manager.comment.dto.CommentResponseDtoFull;
import com.chaplygin.task_manager.comment.model.Comment;
import com.chaplygin.task_manager.task.service.TaskService;
import com.chaplygin.task_manager.user.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserService.class, TaskService.class})
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "author", ignore = true)
    Comment mapCreateDtoToComment(CommentCreateDto commentCreateDto);

    CommentResponseDtoFull mapCommentToCommentResponseDto(Comment comment);
}
