package com.chaplygin.task_manager.task.mapper;

import com.chaplygin.task_manager.task.dto.TaskPagedListResponseDto;
import com.chaplygin.task_manager.task.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", uses = TaskMapper.class)
public interface TaskListMapper {

    @Mapping(source = "content", target = "tasks")
    @Mapping(source = "number", target = "page")
    @Mapping(source = "size", target = "size")
    @Mapping(source = "totalPages", target = "totalPages")
    TaskPagedListResponseDto pageToTasksListResponseDtoPaged(Page<Task> page);
}
