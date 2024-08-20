package com.chaplygin.task_manager.task.controller;

import com.chaplygin.task_manager.task.dto.TaskCreateDto;
import com.chaplygin.task_manager.task.dto.TaskResponseDtoFull;
import com.chaplygin.task_manager.task.mapper.TaskMapper;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.service.TaskService;
import com.chaplygin.task_manager.user.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Tag(name = "Tasks controller", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDtoFull createTask(
            @Valid @RequestBody TaskCreateDto taskCreateDto
    ) {
        Task task = taskMapper.mapCreateDtoToTask(taskCreateDto);
        task.setStatus(Status.NEW);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setOwner(currentUser);
        Task savedTask = taskService.createTask(task);
        return taskMapper.mapTaskToResponseDtoFull(savedTask);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TaskResponseDtoFull> getAllTasks() {
        return taskService.getAllTasks().stream()
                .map(taskMapper::mapTaskToResponseDtoFull)
                .collect(Collectors.toList());
    }
}
