package com.local.service.productmanagement;

public class InvalidProductCountException extends ProductManagementServiceException{
    public InvalidProductCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
