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

    public static SignUpRequest createSignUpRequestNullEmail() {

        return new SignUpRequest(
                null,
                "1",
                "User1"
        );
    }

    public static SignUpRequest createSignUpRequestNullPassword() {

        return new SignUpRequest(
                "user1@domain.com",
                null,
                "User1"
        );
    }

    public static SignUpRequest createSignUpRequestNullUserName() {

        return new SignUpRequest(
                "user1@domain.com",
                "1",
                null
        );
    }

    public static SignUpRequest createSignUpRequestEmailShorterThan6Characters() {

        return new SignUpRequest(
                "u@d.c",
                "1",
                "User1"
        );
    }

    public static SignUpRequest createSignUpRequestEmailLongerThan50Characters() {

        return new SignUpRequest(
                "a".repeat(50) + "@domain.com",
                "1",
                "User1"
        );
    }

    public static SignUpRequest createSignUpRequestPasswordShorterThan1Characters() {

        return new SignUpRequest(
                "user1@domain.com",
                "",
                "User1"
        );
    }

    public static SignUpRequest createSignUpRequestPasswordLongerThan20Characters() {

        return new SignUpRequest(
                "user1@domain.com",
                "a".repeat(21),
                "User1"
        );
    }

    public static SignUpRequest createSignUpRequestUserNameShorterThan3Characters() {

        return new SignUpRequest(
                "user1@domain.com",
                "1",
                "Us"
        );
    }

    public static SignUpRequest createSignUpRequestUserNameLongerThan50Characters() {

        return new SignUpRequest(
                "user1@domain.com",
                "1",
                "User1" + "a".repeat(50)
        );
    }

}
