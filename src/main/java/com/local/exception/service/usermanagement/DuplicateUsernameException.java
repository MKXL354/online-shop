package com.local.exception.service.usermanagement;

public class DuplicateUsernameException extends UserManagementServiceException{
    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}