package com.local.dao.product;

import com.local.commonexceptions.ApplicationException;

public class ProductDAOException extends ApplicationException {
    public ProductDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
