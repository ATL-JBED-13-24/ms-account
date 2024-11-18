package com.spring.account.exception;

public class ResourceNotAccessible extends RuntimeException {
    private final String message;

    public ResourceNotAccessible(String message) {
        super(message);
        this.message = message;
    }
}
