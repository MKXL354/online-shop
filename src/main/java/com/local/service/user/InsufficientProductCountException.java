package com.local.service.user;

public class InsufficientProductCountException extends UserServiceException{
    public InsufficientProductCountException(String message, Throwable cause) {
        super(message, cause);
    }
}