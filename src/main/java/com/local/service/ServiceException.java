package com.local.service;

import com.local.commonexceptions.ApplicationException;

public class ServiceException extends ApplicationException {
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
