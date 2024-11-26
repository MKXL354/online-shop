package com.local.util.transaction;

import com.local.exception.common.ApplicationException;

public class TransactionException extends ApplicationException {
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
