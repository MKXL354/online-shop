package com.local.service.productmanagement;

import com.local.service.ServiceException;

public class ProductManagementServiceException extends ServiceException {
    public ProductManagementServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
