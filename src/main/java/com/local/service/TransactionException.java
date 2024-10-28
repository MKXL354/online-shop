package com.local.service;

import com.local.commonexceptions.ApplicationException;

public class TransactionException extends ApplicationException {
    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }
}
