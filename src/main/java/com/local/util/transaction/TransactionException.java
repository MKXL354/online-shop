package com.local.util.transaction;

import com.local.exception.common.ApplicationRuntimeException;

public class TransactionException extends ApplicationRuntimeException {
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
