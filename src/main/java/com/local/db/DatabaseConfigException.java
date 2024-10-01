package com.local.db;

import com.local.commonexceptions.ServiceRuntimeException;

public class DatabaseConfigException extends ServiceRuntimeException {
    public DatabaseConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
