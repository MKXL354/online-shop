package com.local.db;

import com.local.commonexceptions.ApplicationRuntimeException;

public class DatabaseConfigException extends ApplicationRuntimeException {
    public DatabaseConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
