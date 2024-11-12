package com.local.util.token;

import com.local.exception.common.ApplicationException;

public class InvalidTokenException extends ApplicationException {
    public InvalidTokenException(String message, Throwable cause) {
        super(message, cause);
    }
}
