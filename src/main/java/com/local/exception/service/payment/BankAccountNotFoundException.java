package com.local.exception.service.payment;

public class BankAccountNotFoundException extends PaymentServiceException{
    public BankAccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
