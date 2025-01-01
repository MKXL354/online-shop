package com.local.exception.service.common;

import com.local.exception.common.ApplicationException;

public class CommonServiceException extends ApplicationException {
    public CommonServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
