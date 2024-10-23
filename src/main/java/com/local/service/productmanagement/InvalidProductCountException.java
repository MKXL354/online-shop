package com.local.service.productmanagement;

public class InvalidProductCountException extends ProductManagementServiceException{
    public InvalidProductCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
//TODO: changed condition for price and count in both cart and product dao
