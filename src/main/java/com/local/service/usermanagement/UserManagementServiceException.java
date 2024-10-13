package com.local.service.usermanagement;

import com.local.service.ServiceException;

public class UserManagementServiceException extends ServiceException {
    public UserManagementServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
