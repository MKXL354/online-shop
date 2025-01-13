package com.local.exception.service.payment;

public class InvalidBankAccountException extends PaymentServiceException{
    public InvalidBankAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
