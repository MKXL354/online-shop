package com.local.service.productmanagement;

public class NegativeProductCountException extends ProductManagementServiceException{
    public NegativeProductCountException(String message, Throwable cause) {
        super(message, cause);
    }
}
//TODO: messages inside the exception itself? concentration of logic?
