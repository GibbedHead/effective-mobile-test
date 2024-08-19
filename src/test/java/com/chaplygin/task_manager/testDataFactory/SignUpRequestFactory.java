package com.chaplygin.task_manager.testDataFactory;

import com.chaplygin.task_manager.user.dto.SignUpRequest;

public class SignUpRequestFactory {

    public static SignUpRequest createSignUpRequest() {

        return new SignUpRequest(
                "user1@domain.com",
                "1",
                "User1"
        );
    }
}
