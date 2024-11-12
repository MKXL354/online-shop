package com.local.exception.service.usermanagement;

import com.local.exception.service.ServiceException;

public class UserManagementServiceException extends ServiceException {
    public UserManagementServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
