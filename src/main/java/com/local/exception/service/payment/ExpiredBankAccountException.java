package com.local.exception.service.payment;

public class ExpiredBankAccountException extends PaymentServiceException{
    public ExpiredBankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
