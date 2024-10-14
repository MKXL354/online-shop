package com.local.servlet;

import com.local.commonexceptions.ApplicationException;

public class InvalidRequestObjectException extends ApplicationException {
    public InvalidRequestObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
