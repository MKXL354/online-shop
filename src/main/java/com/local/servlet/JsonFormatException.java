package com.local.servlet;

import com.local.commonexceptions.ApplicationException;

public class JsonFormatException extends ApplicationException {
    public JsonFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
