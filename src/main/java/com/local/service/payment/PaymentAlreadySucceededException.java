package com.local.service.payment;

public class PaymentAlreadySucceededException extends PaymentServiceException{
    public PaymentAlreadySucceededException(String message, Throwable cause) {
        super(message, cause);
    }
}
