package com.chaplygin.task_manager.testDataFactory;

import com.chaplygin.task_manager.user.dto.SignInRequest;

public class SignInRequestFactory {

    public static SignInRequest createSignInRequest() {

        return new SignInRequest(
                "user1@domain.com",
                "1"
        );
    }
}
