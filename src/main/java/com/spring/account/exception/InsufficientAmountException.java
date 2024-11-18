package com.spring.account.exception;

public class InsufficientAmountException extends RuntimeException {
    private final String message;

    public InsufficientAmountException(String message) {
        super(message);
        this.message = message;
    }
}
