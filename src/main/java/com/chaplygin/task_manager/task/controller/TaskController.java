package com.chaplygin.task_manager.task.controller;

import com.chaplygin.task_manager.comment.dto.CommentCreateDto;
import com.chaplygin.task_manager.comment.dto.CommentPagedListResponseDto;
import com.chaplygin.task_manager.comment.mapper.CommentListMapper;
import com.chaplygin.task_manager.comment.mapper.CommentMapper;
import com.chaplygin.task_manager.comment.model.Comment;
import com.chaplygin.task_manager.comment.service.CommentService;
import com.chaplygin.task_manager.exception.model.AppErrorResponse;
import com.chaplygin.task_manager.permission.annotation.CheckTaskPermission;
import com.chaplygin.task_manager.task.dto.*;
import com.chaplygin.task_manager.task.mapper.TaskListMapper;
import com.chaplygin.task_manager.task.mapper.TaskMapper;
import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.service.TaskService;
import com.chaplygin.task_manager.user.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Create task", description = "Creating task and returning DTO",
            responses = {
                    @ApiResponse(description = "Object, containing new task DTO",
                            responseCode = "201",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDtoNoComments.class)
                            )),
                    @ApiResponse(responseCode = "400", description = "Bad request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            })
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

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves a paginated list of tasks with optional filters for title, description," +
                    " status, and priority. The list can be sorted by various fields in ascending or descending order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved paginated list of tasks",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskPagedListResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Server Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TaskPagedListResponseDto getAllTasks(
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page,

            @Parameter(description = "Number of tasks per page", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "Filter by task title", example = "My Task")
            @RequestParam(value = "title", required = false) String title,

            @Parameter(description = "Filter by task description", example = "This is a sample task description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Filter by task status", example = "IN_PROGRESS")
            @RequestParam(value = "status", required = false) Status status,

            @Parameter(description = "Filter by task priority", example = "HIGH")
            @RequestParam(value = "priority", required = false) Priority priority,

            @Parameter(description = "Field to sort by", example = "id")
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: 'asc' for ascending, 'desc' for descending", example = "asc")
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getAllTasks(
                page, size, title, description, status, priority, sortBy, sortDirection
        );
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @Operation(
            summary = "Get tasks for a specific user",
            description = "Retrieves a paginated list of tasks assigned or owned by a specific user, with optional" +
                    " filters for title, description, status, and priority. The list can be sorted by various" +
                    " fields in ascending or descending order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved paginated list of tasks for the user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskPagedListResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters or user ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "User not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Server Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @GetMapping("/user/{userId}")
    public TaskPagedListResponseDto getTasksForUser(
            @Parameter(description = "ID of the user whose tasks are to be retrieved", example = "1")
            @PathVariable Long userId,

            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page,

            @Parameter(description = "Number of tasks per page", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "Filter by task title", example = "My Task")
            @RequestParam(value = "title", required = false) String title,

            @Parameter(description = "Filter by task description", example = "This is a sample task description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Filter by task status", example = "IN_PROGRESS")
            @RequestParam(value = "status", required = false) Status status,

            @Parameter(description = "Filter by task priority", example = "HIGH")
            @RequestParam(value = "priority", required = false) Priority priority,

            @Parameter(description = "Field to sort by", example = "id")
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: 'asc' for ascending, 'desc' for descending", example = "asc")
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getTasksForUser(
                page, size, title, description, status, priority, sortBy, sortDirection, userId
        );
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @Operation(
            summary = "Get tasks owned by a specific user",
            description = "Retrieves a paginated list of tasks owned by a specific user, with optional" +
                    " filters for title, description, status, and priority. The list can be sorted by various" +
                    " fields in ascending or descending order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved paginated list of tasks for the user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskPagedListResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters or user ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "User not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Server Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @GetMapping("/owner/{userId}")
    public TaskPagedListResponseDto getTasksForOwner(
            @Parameter(description = "ID of the user whose tasks are to be retrieved", example = "1")
            @PathVariable Long userId,

            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page,

            @Parameter(description = "Number of tasks per page", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "Filter by task title", example = "My Task")
            @RequestParam(value = "title", required = false) String title,

            @Parameter(description = "Filter by task description", example = "This is a sample task description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Filter by task status", example = "IN_PROGRESS")
            @RequestParam(value = "status", required = false) Status status,

            @Parameter(description = "Filter by task priority", example = "HIGH")
            @RequestParam(value = "priority", required = false) Priority priority,

            @Parameter(description = "Field to sort by", example = "id")
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: 'asc' for ascending, 'desc' for descending", example = "asc")
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getTasksForOwner(
                page, size, title, description, status, priority, sortBy, sortDirection, userId
        );
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @Operation(
            summary = "Get tasks assigned to a specific user",
            description = "Retrieves a paginated list of tasks assigned to a specific user, with optional" +
                    " filters for title, description, status, and priority. The list can be sorted by various" +
                    " fields in ascending or descending order.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved paginated list of tasks for the user",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskPagedListResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request parameters or user ID",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "User not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Server Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @GetMapping("/assignee/{userId}")
    public TaskPagedListResponseDto getTasksForAssignee(
            @Parameter(description = "ID of the user whose tasks are to be retrieved", example = "1")
            @PathVariable Long userId,

            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(value = "page", defaultValue = "1") int page,

            @Parameter(description = "Number of tasks per page", example = "10")
            @RequestParam(value = "size", defaultValue = "10") int size,

            @Parameter(description = "Filter by task title", example = "My Task")
            @RequestParam(value = "title", required = false) String title,

            @Parameter(description = "Filter by task description", example = "This is a sample task description")
            @RequestParam(value = "description", required = false) String description,

            @Parameter(description = "Filter by task status", example = "IN_PROGRESS")
            @RequestParam(value = "status", required = false) Status status,

            @Parameter(description = "Filter by task priority", example = "HIGH")
            @RequestParam(value = "priority", required = false) Priority priority,

            @Parameter(description = "Field to sort by", example = "id")
            @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,

            @Parameter(description = "Sort direction: 'asc' for ascending, 'desc' for descending", example = "asc")
            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
    ) {
        Page<Task> taskPage = taskService.getTasksForAssignee(
                page, size, title, description, status, priority, sortBy, sortDirection, userId
        );
        return taskListMapper.pageToTaskPagedListResponseDto(taskPage);
    }

    @Operation(
            summary = "Get task by ID",
            description = "Retrieves the details of a specific task by its ID, excluding comments.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved task details",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDtoNoComments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments getTaskById(
            @Parameter(description = "ID of the task to be retrieved", example = "1")
            @PathVariable Long id
    ) {
        return taskMapper.mapTaskToResponseDtoNoComments(taskService.getTaskById(id));
    }

    @Operation(
            summary = "Delete task by ID",
            description = "Deletes a specific task identified by its ID. If the task is successfully deleted," +
                    " the server will respond with a 204 No Content status. If the task is not found or if " +
                    "there is an error, an appropriate error status will be returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Task successfully deleted",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @CheckTaskPermission(action = "delete")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskById(
            @Parameter(description = "ID of the task to be deleted", example = "1")
            @PathVariable Long id
    ) {
        taskService.deleteTaskById(id);
    }

    @Operation(
            summary = "Update task by ID",
            description = "Updates the details of a specific task identified by its ID. The task details are" +
                    " provided in the request body. On success, the updated task is returned. If the task is " +
                    "not found or if there is an error, an appropriate error status is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated the task",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDtoNoComments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @CheckTaskPermission(action = "updateAll")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments updateTask(
            @Parameter(description = "Details for updating the task", required = true)
            @Valid @RequestBody TaskFullUpdateDto fullUpdateDto,
            @Parameter(description = "ID of the task to be updated", example = "1")
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromFull(fullUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @Operation(
            summary = "Update task status by ID",
            description = "Updates the status of a specific task identified by its ID. The new status is provided" +
                    " in the request body. On success, the updated task is returned. If the task is not found or " +
                    "if there is an error, an appropriate error status is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated the task status",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDtoNoComments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @CheckTaskPermission(action = "updateStatus")
    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments updateTaskStatus(
            @Parameter(description = "Details for updating the task status", required = true)
            @Valid @RequestBody TaskStatusUpdateDto statusUpdateDto,
            @Parameter(description = "ID of the task whose status is to be updated", example = "1")
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromStatus(statusUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @Operation(
            summary = "Update task assignee by ID",
            description = "Updates the assignee of a specific task identified by its ID. The new assignee details" +
                    " are provided in the request body. On success, the updated task is returned. If the task is" +
                    " not found or if there is an error, an appropriate error status is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully updated the task assignee",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDtoNoComments.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @CheckTaskPermission(action = "updateAssignee")
    @PatchMapping("/{id}/assignee")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoNoComments updateTaskAssignee(
            @Parameter(description = "Details for updating the task assignee", required = true)
            @Valid @RequestBody TaskAssigneeUpdateDto assigneeUpdateDto,
            @Parameter(description = "ID of the task whose assignee is to be updated", example = "1")
            @PathVariable Long id
    ) {
        Task foundTask = taskService.getTaskById(id);
        taskMapper.partialUpdateFromAssignee(assigneeUpdateDto, foundTask);
        Task savedTask = taskService.saveTask(foundTask);
        return taskMapper.mapTaskToResponseDtoNoComments(savedTask);
    }

    @Operation(
            summary = "Add a comment to a task by ID",
            description = "Adds a comment to a specific task identified by its ID. The comment details are provided " +
                    "in the request body. On success, the updated task with the new comment is returned. " +
                    "If the task is not found or if there is an error, an appropriate error status is returned.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully added the comment to the task",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = TaskResponseDtoFull.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @PostMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public TaskResponseDtoFull createComment(
            @Parameter(description = "ID of the task to which the comment will be added", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Details of the comment to be created", required = true)
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

    @Operation(
            summary = "Get comments by task ID",
            description = "Retrieves a paginated list of comments for a specific task identified by its ID. " +
                    "You can specify pagination parameters to control the number of comments returned per " +
                    "page and the page number.",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID of the task for which comments are to be retrieved",
                            example = "1",
                            in = ParameterIn.PATH,
                            required = true
                    ),
                    @Parameter(
                            name = "page",
                            description = "Page number to retrieve, starting from 1",
                            example = "1",
                            in = ParameterIn.QUERY
                    ),
                    @Parameter(
                            name = "size",
                            description = "Number of comments per page",
                            example = "10",
                            in = ParameterIn.QUERY
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved the paginated list of comments",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentPagedListResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid task ID supplied",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 400,
                                                "message": "Validation Error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Task not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 404,
                                                "message": "Task not found",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppErrorResponse.class),
                                    examples = @ExampleObject(value = """
                                            {
                                                "status": 500,
                                                "message": "Internal server error",
                                                "timestamp": "2024-06-04T22:40:46.7924577"
                                            }""")
                            )
                    )
            }
    )
    @GetMapping("/{id}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentPagedListResponseDto getCommentsByTaskId(
            @PathVariable Long id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Comment> commentPage = commentService.getCommentsByTaskId(id, page, size);
        return commentListMapper.pageToCommentPagedListResponseDto(commentPage);
    }
}
