package com.local.exception.service.common;

import com.local.exception.common.ApplicationException;

public class CartNotFoundException extends ApplicationException {
    public CartNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
