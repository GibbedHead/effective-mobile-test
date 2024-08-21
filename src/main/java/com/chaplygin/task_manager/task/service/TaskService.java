package com.chaplygin.task_manager.task.service;

import com.chaplygin.task_manager.exception.model.TaskNotFoundException;
import com.chaplygin.task_manager.task.model.Priority;
import com.chaplygin.task_manager.task.model.Status;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.repository.TaskRepository;
import com.chaplygin.task_manager.task.specification.TaskSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public Page<Task> getAllTasks(
            int page, int size,
            String title, String description,
            Status status, Priority priority,
            String sortBy, String sortDirection
    ) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
        Specification<Task> spec = Specification.where(TaskSpecifications.hasTitle(title))
                .and(TaskSpecifications.hasDescription(description))
                .and(TaskSpecifications.hasStatus(status))
                .and(TaskSpecifications.hasPriority(priority));
        return taskRepository.findAll(spec, pageable);
    }

    @Transactional
    public Task getTaskById(long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task id=%d not found".formatted(id)));
    }

    @Transactional
    public void deleteTaskById(long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task id=%d not found".formatted(id)));
        taskRepository.delete(task);
    }
}
