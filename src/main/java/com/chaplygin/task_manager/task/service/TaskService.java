package com.chaplygin.task_manager.task.service;

import com.chaplygin.task_manager.exception.model.TaskNotFoundException;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
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
