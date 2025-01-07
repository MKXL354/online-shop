package com.local.persistence.transaction;

import com.local.exception.common.ApplicationException;

public class TransactionManagerException extends ApplicationException {
    public TransactionManagerException(String message, Throwable cause) {
        super(message, cause);
    }
}
