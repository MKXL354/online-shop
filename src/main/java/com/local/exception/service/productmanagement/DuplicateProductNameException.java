package com.local.exception.service.productmanagement;

public class DuplicateProductNameException extends ProductManagementServiceException{
    public DuplicateProductNameException(String message, Throwable cause) {
        super(message, cause);
    }
}
