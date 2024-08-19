package com.chaplygin.task_manager.exception.model;

public class AccessTokenSignatureException extends RuntimeException {
    public AccessTokenSignatureException(String message) {
        super(message);
    }
}
