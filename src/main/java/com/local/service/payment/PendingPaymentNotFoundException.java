package com.local.service.payment;

public class PendingPaymentNotFoundException extends PaymentServiceException {
    public PendingPaymentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
