package com.login.task.exception;

import org.springframework.http.HttpStatus;

public class TaskException extends RuntimeException {
    private final HttpStatus status;

    public TaskException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
} 