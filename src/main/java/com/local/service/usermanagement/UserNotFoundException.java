package com.local.service.usermanagement;

public class UserNotFoundException extends UserManagementServiceException{
    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
