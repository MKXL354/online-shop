package com.local.service.payment;

public class ActivePaymentNotFoundException extends PaymentServiceException {
    public ActivePaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
