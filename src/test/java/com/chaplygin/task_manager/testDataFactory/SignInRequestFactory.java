package com.chaplygin.task_manager.testDataFactory;

import com.chaplygin.task_manager.user.dto.SignInRequest;

public class SignInRequestFactory {

    public static SignInRequest createSignInRequest() {

        return new SignInRequest(
                "user1@domain.com",
                "1"
        );
    }

    public static SignInRequest createSignInRequestNullEmail() {

        return new SignInRequest(
                null,
                "1"
        );
    }

    public static SignInRequest createSignInRequestNullPassword() {

        return new SignInRequest(
                "user1@domain.com",
                null
        );
    }

    public static SignInRequest createSignInRequestEmailShorterThan6Characters() {

        return new SignInRequest(
                "u@d.c",
                "1"
        );
    }

    public static SignInRequest createSignInRequestEmailLongerThan50Characters() {

        return new SignInRequest(
                "a".repeat(50) + "@domain.com",
                "1"
        );
    }

    public static SignInRequest createSignInRequestPasswordShorterThan1Characters() {

        return new SignInRequest(
                "user1@domain.com",
                ""
        );
    }

    public static SignInRequest createSignInRequestPasswordLongerThan20Characters() {

        return new SignInRequest(
                "user1@domain.com",
                "a".repeat(21)
        );
    }

}
