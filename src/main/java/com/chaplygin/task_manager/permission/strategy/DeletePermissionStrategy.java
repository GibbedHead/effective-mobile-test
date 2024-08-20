package com.chaplygin.task_manager.permission.strategy;

import com.chaplygin.task_manager.exception.model.AccessDeniedException;
import com.chaplygin.task_manager.task.model.Task;
import com.chaplygin.task_manager.user.model.Role;
import com.chaplygin.task_manager.user.model.User;
import org.springframework.stereotype.Component;

@Component("delete")
public class DeletePermissionStrategy implements TaskPermissionStrategy {

    @Override
    public void checkPermission(Task task, User user) {
        if (!(isUserAdmin(user) || isUserOwner(user, task))) {
            throw new AccessDeniedException("You do not have permission to delete this task.");
        }
    }

    private boolean isUserAdmin(User user) {
        return user.getRole() == Role.ROLE_ADMIN;
    }

    private boolean isUserOwner(User user, Task task) {
        return task.getOwner().equals(user);
    }
}