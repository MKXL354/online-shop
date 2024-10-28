package com.local.service.user;

import com.local.commonexceptions.ApplicationException;

public class UserServiceException extends ApplicationException {
    public UserServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
