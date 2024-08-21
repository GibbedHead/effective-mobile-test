package com.chaplygin.task_manager.permission.aspect;

import com.chaplygin.task_manager.permission.annotation.CheckTaskPermission;
import com.chaplygin.task_manager.permission.strategy.TaskPermissionStrategy;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.task.service.TaskService;
import com.chaplygin.task_manager.user.model.User;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class TaskPermissionAspect {
    private final TaskService taskService;
    private final Map<String, TaskPermissionStrategy> strategies;

    @Before("@annotation(checkTaskPermission) && args(.., taskId)")
    public void checkPermission(CheckTaskPermission checkTaskPermission, Long taskId) {
        String action = checkTaskPermission.action();
        Task task = taskService.getTaskById(taskId);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        TaskPermissionStrategy strategy = strategies.get(action);
        if (strategy == null) {
            throw new UnsupportedOperationException("Unknown permission action: " + action);
        }
        strategy.checkPermission(task, currentUser);
    }
}
