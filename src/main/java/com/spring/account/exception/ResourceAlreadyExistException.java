package com.spring.account.exception;

public class ResourceAlreadyExistException extends RuntimeException {
    private final String message;

    public ResourceAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
