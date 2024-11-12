package com.local.exception.service.usermanagement;

public class WrongPasswordException extends UserManagementServiceException{
    public WrongPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
