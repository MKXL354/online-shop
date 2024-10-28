package com.local.service.user;

public class InsufficientBalanceException extends UserServiceException {
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
