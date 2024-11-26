package com.local.exception.common;

public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
