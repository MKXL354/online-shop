package com.local.service.productmanagement;

public class ProductNotFoundException extends ProductManagementServiceException {
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
