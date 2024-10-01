package com.local.commonexceptions;

public class ServiceRuntimeException extends RuntimeException {
    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
