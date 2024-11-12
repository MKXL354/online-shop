package com.local.exception.service;

import com.local.exception.common.ApplicationException;

public class ServiceException extends ApplicationException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
