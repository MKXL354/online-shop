package com.local.service.productmanagement;

public class InvalidProductPriceException extends ProductManagementServiceException{
    public InvalidProductPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
