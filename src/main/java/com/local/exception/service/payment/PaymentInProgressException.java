package com.local.exception.service.payment;

public class PaymentInProgressException extends PaymentServiceException {
    public PaymentInProgressException(String message, Throwable cause) {
        super(message, cause);
    }
}
