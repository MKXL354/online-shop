package com.local.commonexceptions;

public class ApplicationRuntimeException extends RuntimeException {
    public ApplicationRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
