package com.local.dao.transaction;

import com.local.exception.common.ApplicationRuntimeException;

public class BadTransactionException extends ApplicationRuntimeException {
    public BadTransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
