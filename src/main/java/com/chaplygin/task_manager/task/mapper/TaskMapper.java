package com.chaplygin.task_manager.task.mapper;

import com.chaplygin.task_manager.task.dto.*;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.user.service.UserService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserService.class})
public interface TaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(target = "status", ignore = true)
    Task mapCreateDtoToTask(TaskCreateDto taskCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdateFromFull(TaskFullUpdateDto taskCreateDto, @MappingTarget Task task);

    void partialUpdateFromStatus(TaskStatusUpdateDto taskCreateDto, @MappingTarget Task task);

    @Mapping(source = "assigneeId", target = "assignee")
    void partialUpdateFromAssignee(TaskAssigneeUpdateDto taskCreateDto, @MappingTarget Task task);

    TaskResponseDtoNoComments mapTaskToResponseDtoNoComments(Task task);

    TaskResponseDtoFull mapTaskToResponseDtoFull(Task task);

}
