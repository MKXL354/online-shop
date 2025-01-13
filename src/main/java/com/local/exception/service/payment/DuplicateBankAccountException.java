package com.local.exception.service.payment;

public class DuplicateBankAccountException extends PaymentServiceException{
    public DuplicateBankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
