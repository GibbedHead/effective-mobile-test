package com.chaplygin.task_manager.task.repository;

import com.chaplygin.task_manager.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}