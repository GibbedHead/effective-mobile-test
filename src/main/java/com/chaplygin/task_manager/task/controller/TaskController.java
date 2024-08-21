package com.chaplygin.task_manager.task.controller;

import com.chaplygin.task_manager.comment.dto.CommentCreateDto;
import com.chaplygin.task_manager.comment.dto.CommentPagedListResponseDto;
import com.chaplygin.task_manager.comment.mapper.CommentListMapper;
import com.chaplygin.task_manager.comment.mapper.CommentMapper;
import com.chaplygin.task_manager.comment.model.Comment;
import com.chaplygin.task_manager.comment.service.CommentService;
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
    private final CommentService commentService;
    private final TaskMapper taskMapper;
    private final TaskListMapper taskListMapper;
    private final CommentMapper commentMapper;
    private final CommentListMapper commentListMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDtoNoComments createTask(
            @Valid @RequestBody TaskCreateDto taskCreateDto
    ) {
        Task task = taskMapper.mapCreateDtoToTask(taskCreateDto);
        task.setStatus(Status.NEW);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setOwner(currentUser);
        Task savedTask = taskService.saveTask(task);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskPagedListResponseDto getAllTasks(
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
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @GetMapping("/user/{userId}")
    public TaskPagedListResponseDto getTasksForUser(
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
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @GetMapping("/owner/{userId}")
    public TaskPagedListResponseDto getTasksForOwner(
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
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @GetMapping("/assignee/{userId}")
    public TaskPagedListResponseDto getTasksForAssignee(
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
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments getTaskById(@PathVariable Long id) {
        return taskMapper.mapTaskToResponseDtoNoComments(taskService.getTaskById(id));
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
    public TaskResponseDtoNoComments updateTask(
            @Valid @RequestBody TaskFullUpdateDto fullUpdateDto,
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromFull(fullUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @CheckTaskPermission(action = "updateStatus")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments updateTaskStatus(
            @Valid @RequestBody TaskStatusUpdateDto statusUpdateDto,
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromStatus(statusUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @CheckTaskPermission(action = "updateAssignee")
    @PatchMapping("/{id}/assignee")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments updateTaskAssignee(
            @Valid @RequestBody TaskAssigneeUpdateDto assigneeUpdateDto,
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromAssignee(assigneeUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoFull createComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentCreateDto commentCreateDto
    ) {
        Task foundTask = taskService.getTaskById(id);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentMapper.mapCreateDtoToComment(commentCreateDto);
        comment.setTask(foundTask);
        comment.setAuthor(currentUser);
        Comment savedComment = commentService.saveComment(comment);
        foundTask.getComments().add(savedComment);
        return taskMapper.mapTaskToResponseDtoFull(foundTask);
    }

    @GetMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentPagedListResponseDto getComments(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Comment> commentPage = commentService.getCommentsByTaskId(id, page, size);
        return commentListMapper.pageToCommentPagedListResponseDto(commentPage);
    }
}
