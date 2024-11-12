package com.local.servlet;

import com.local.exception.common.ApplicationException;

public class JsonFormatException extends ApplicationException {
    public JsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
