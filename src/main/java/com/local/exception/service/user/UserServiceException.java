package com.local.exception.service.user;

import com.local.exception.service.ServiceException;

public class UserServiceException extends ServiceException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
