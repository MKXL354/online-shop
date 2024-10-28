package com.local.service.user;

import com.local.service.user.UserServiceException;

public class InsufficientBalanceException extends UserServiceException {
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
