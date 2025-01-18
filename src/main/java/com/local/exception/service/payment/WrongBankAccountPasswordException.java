package com.local.exception.service.payment;

public class WrongBankAccountPasswordException extends PaymentServiceException {
    public WrongBankAccountPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}

