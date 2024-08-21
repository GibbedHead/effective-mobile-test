package com.chaplygin.task_manager.task.controller;

import com.chaplygin.task_manager.permission.annotation.CheckTaskPermission;
import com.chaplygin.task_manager.task.dto.*;
import com.chaplygin.task_manager.task.mapper.TaskListMapper;
import com.chaplygin.task_manager.task.mapper.TaskMapper;
import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.service.TaskService;
import com.chaplygin.task_manager.user.model.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
@Tag(name = "Tasks controller", description = "Endpoints for managing tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskListMapper taskListMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDtoFull createTask(
            @Valid @RequestBody TaskCreateDto taskCreateDto
    ) {
        Task task = taskMapper.mapCreateDtoToTask(taskCreateDto);
        task.setStatus(Status.NEW);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setOwner(currentUser);
        Task savedTask = taskService.saveTask(task);
        return taskMapper.mapTaskToResponseDtoFull(savedTask);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TasksListResponseDtoPaged getAllTasks(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getAllTasks(
                page, size, title, description, status, priority, sortBy, sortDirection
        );
        return taskListMapper.pageToTasksListResponseDtoPaged(taskPage);
    }

    @GetMapping("/user/{userId}")
    public TasksListResponseDtoPaged getTasksForUser(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getTasksForUser(
                page, size, title, description, status, priority, sortBy, sortDirection, userId
        );
        return taskListMapper.pageToTasksListResponseDtoPaged(taskPage);
    }

    @GetMapping("/owner/{userId}")
    public TasksListResponseDtoPaged getTasksForOwner(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getTasksForOwner(
                page, size, title, description, status, priority, sortBy, sortDirection, userId
        );
        return taskListMapper.pageToTasksListResponseDtoPaged(taskPage);
    }

    @GetMapping("/assignee/{userId}")
    public TasksListResponseDtoPaged getTasksForAssignee(
            @PathVariable Long userId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) Status status,
            @RequestParam(value = "priority", required = false) Priority priority,
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getTasksForAssignee(
                page, size, title, description, status, priority, sortBy, sortDirection, userId
        );
        return taskListMapper.pageToTasksListResponseDtoPaged(taskPage);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoFull getTaskById(@PathVariable Long id) {
        return taskMapper.mapTaskToResponseDtoFull(taskService.getTaskById(id));
    }

    @CheckTaskPermission(action = "delete")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
    }

    @CheckTaskPermission(action = "updateAll")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoFull updateTask(
            @Valid @RequestBody TaskFullUpdateDto fullUpdateDto,
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromFull(fullUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoFull(savedTask);
    }

    @CheckTaskPermission(action = "updateStatus")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoFull updateTaskStatus(
            @Valid @RequestBody TaskStatusUpdateDto statusUpdateDto,
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromStatus(statusUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoFull(savedTask);
    }

    @CheckTaskPermission(action = "updateAssignee")
    @PatchMapping("/{id}/assignee")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoFull updateTaskAssignee(
            @Valid @RequestBody TaskAssigneeUpdateDto assigneeUpdateDto,
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromAssignee(assigneeUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoFull(savedTask);
    }
}
