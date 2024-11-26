package com.local.servlet.validation;

import com.local.exception.common.ApplicationException;

public class InvalidRequestObjectException extends ApplicationException {
    public InvalidRequestObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
