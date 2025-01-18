package com.local.exception.service.usermanagement;

public class WrongUserPasswordException extends UserManagementServiceException{
    public WrongUserPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
