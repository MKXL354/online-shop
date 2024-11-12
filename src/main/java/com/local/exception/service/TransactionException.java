package com.local.exception.service;

import com.local.exception.common.ApplicationException;

public class TransactionException extends ApplicationException {
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
