package com.local.exception.service.payment;

public class PendingPaymentNotFoundException extends PaymentServiceException {
    public PendingPaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
