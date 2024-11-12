package com.local.exception.service.user;

public class EmptyCartException extends UserServiceException{
    public EmptyCartException(String message, Throwable cause) {
        super(message, cause);
    }
}
