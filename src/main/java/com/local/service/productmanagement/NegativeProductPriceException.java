package com.local.service.productmanagement;

public class NegativeProductPriceException extends ProductManagementServiceException{
    public NegativeProductPriceException(String message, Throwable cause) {
        super(message, cause);
    }
}
