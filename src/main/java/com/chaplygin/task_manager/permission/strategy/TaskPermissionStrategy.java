package com.chaplygin.task_manager.permission.strategy;

import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.user.model.User;

public interface TaskPermissionStrategy {
    void checkPermission(Task task, User user);
}
