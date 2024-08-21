package com.chaplygin.task_manager.testDataFactory;

import com.chaplygin.task_manager.task.dto.TaskCreateDto;
import com.chaplygin.task_manager.task.dto.TaskPagedListResponseDto;
import com.chaplygin.task_manager.task.dto.TaskResponseDtoNoComments;
import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public class TaskFactory {

    public static TaskCreateDto createValidTaskCreateDto() {
        return new TaskCreateDto(
                "Test task",
                "Test description",
                1L,
                Priority.MEDIUM
        );
    }

    public static TaskCreateDto createInvalidTaskCreateDto() {
        return new TaskCreateDto(
                "",
                "",
                null,
                Priority.MEDIUM
        );
    }

    public static TaskResponseDtoNoComments createTaskResponseDtoNoComments() {
        return new TaskResponseDtoNoComments(
                1L,
                "Test task",
                "Test description",
                UserFactory.createUserResponseDtoFull(),
                UserFactory.createUserResponseDtoFull(),
                Status.IN_PROGRESS,
                Priority.MEDIUM

        );
    }

    public static Task createTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test task");
        task.setDescription("Test description");
        task.setOwner(UserFactory.createUser1Saved());
        task.setAssignee(UserFactory.createUser1Saved());
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.MEDIUM);
        return task;
    }

    public static Task createNewTask1() {
        Task task = new Task();
        task.setTitle("Test task1");
        task.setDescription("Test description1");
        task.setOwner(UserFactory.createUser1Saved());
        task.setAssignee(UserFactory.createUser1Saved());
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.MEDIUM);
        return task;
    }

    public static Task createNewTask2() {
        Task task = new Task();
        task.setTitle("Test task2");
        task.setDescription("Test description2");
        task.setOwner(UserFactory.createUser1Saved());
        task.setAssignee(UserFactory.createUser1Saved());
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.MEDIUM);
        return task;
    }

    public static Task createInvalidNewTask() {
        Task task = new Task();
        task.setTitle("");
        task.setDescription("");
        task.setOwner(null);
        task.setAssignee(null);
        task.setStatus(Status.IN_PROGRESS);
        task.setPriority(Priority.MEDIUM);
        return task;
    }

    public static TaskPagedListResponseDto createTaskPagedListResponseDto() {
        return new TaskPagedListResponseDto(
                List.of(TaskFactory.createTaskResponseDtoNoComments()),
                0,
                10,
                1
        );
    }

    public static Page<Task> createTaskPage() {
        return new PageImpl<>(List.of(TaskFactory.createTask()));
    }
}
