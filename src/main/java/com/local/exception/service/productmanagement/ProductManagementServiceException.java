package com.local.exception.service.productmanagement;

import com.local.exception.service.ServiceException;

public class ProductManagementServiceException extends ServiceException {
    public ProductManagementServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
