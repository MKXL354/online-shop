package com.local.service.user;

import com.local.service.ServiceException;

public class UserServiceException extends ServiceException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
