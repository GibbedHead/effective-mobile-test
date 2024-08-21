package com.chaplygin.task_manager.task.mapper;

import com.chaplygin.task_manager.task.dto.TaskCreateDto;
import com.chaplygin.task_manager.task.dto.TaskFullUpdateDto;
import com.chaplygin.task_manager.task.dto.TaskResponseDtoFull;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.user.service.UserService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = UserService.class)
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(target = "status", ignore = true)
    Task mapCreateDtoToTask(TaskCreateDto taskCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(TaskFullUpdateDto taskCreateDto, @MappingTarget Task task);

    TaskResponseDtoFull mapTaskToResponseDtoFull(Task task);

}
