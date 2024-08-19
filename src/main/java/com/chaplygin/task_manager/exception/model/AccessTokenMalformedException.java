package com.chaplygin.task_manager.exception.model;

public class AccessTokenMalformedException extends RuntimeException {
    public AccessTokenMalformedException(String message) {
        super(message);
    }
}
