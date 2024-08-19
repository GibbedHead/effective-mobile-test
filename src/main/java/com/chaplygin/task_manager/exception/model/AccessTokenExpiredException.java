package com.chaplygin.task_manager.exception.model;

public class AccessTokenExpiredException extends RuntimeException {
    public AccessTokenExpiredException(String message) {
        super(message);
    }
}
