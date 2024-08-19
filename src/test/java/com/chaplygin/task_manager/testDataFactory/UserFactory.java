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

    public static User createUser1FromRequestPrepared() {
        User user = new User();
        user.setEmail("user1@domain.com");
        user.setUsername("User1");
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(Role.ROLE_USER);
        return user;
    }

    public static User createUser1WithNullEmailFromRequestPrepared() {
        User user = new User();
        user.setEmail(null);
        user.setUsername("User1");
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(Role.ROLE_USER);
        return user;
    }

    public static User createUser1WithNullUserNameFromRequestPrepared() {
        User user = new User();
        user.setEmail("user1@domain.com");
        user.setUsername(null);
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(Role.ROLE_USER);
        return user;
    }

    public static User createUser1WithNullRoleFromRequestPrepared() {
        User user = new User();
        user.setEmail("user1@domain.com");
        user.setUsername("User1");
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(null);
        return user;
    }

    public static User createUser1WithNullPasswordFromRequestPrepared() {
        User user = new User();
        user.setEmail("user1@domain.com");
        user.setUsername("User1");
        user.setPassword(null);
        user.setRole(Role.ROLE_USER);
        return user;
    }

    public static User createUser2FromRequestPreparedNonUniqueEmail() {
        User user = new User();
        user.setEmail("user1@domain.com");
        user.setUsername("User2");
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(Role.ROLE_USER);
        return user;
    }

    public static User createUser2FromRequestPreparedNonUniqueUserName() {
        User user = new User();
        user.setEmail("user2@domain.com");
        user.setUsername("User1");
        user.setPassword("$2a$10$rsZZyAVePBUTZrtNelhVfu5ZVSERN.VCSsnqLIoKCVYXDgpGDOGZK");
        user.setRole(Role.ROLE_USER);
        return user;
    }
}
