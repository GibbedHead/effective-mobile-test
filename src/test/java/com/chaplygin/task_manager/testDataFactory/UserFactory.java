package com.chaplygin.task_manager.testDataFactory;

import com.chaplygin.task_manager.user.model.Role;
import com.chaplygin.task_manager.user.model.User;

public class UserFactory {

    public static User createUser1FromRequest() {
        User user = new User();
        user.setEmail("user1@domain.com");
        user.setUsername("User1");
        user.setPassword("1");
        return user;
    }

    public static User createUser1Saved() {
        User user = new User();
        user.setId(1L);
        user.setEmail("user1@domain.com");
        user.setUsername("User1");
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(Role.ROLE_USER);
        return user;
    }
}
