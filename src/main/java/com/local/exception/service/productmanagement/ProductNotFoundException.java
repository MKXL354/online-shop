package com.local.exception.service.productmanagement;

public class ProductNotFoundException extends ProductManagementServiceException {
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
