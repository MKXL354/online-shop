package com.local.exception.common;

public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
//TODO: usage in services and validation?
