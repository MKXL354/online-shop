package com.local.service.productmanagement;

public class NonPositiveProductCountException extends ProductManagementServiceException{
    public NonPositiveProductCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
