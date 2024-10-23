package com.local.service.productmanagement;

public class NonPositiveProductPriceException extends ProductManagementServiceException{
    public NonPositiveProductPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
