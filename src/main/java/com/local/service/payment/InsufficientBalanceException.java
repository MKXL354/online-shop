package com.local.service.payment;

public class InsufficientBalanceException extends PaymentServiceException {
    public InsufficientBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}