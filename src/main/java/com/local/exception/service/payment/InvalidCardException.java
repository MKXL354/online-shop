package com.local.exception.service.payment;

public class InvalidCardException extends PaymentServiceException{
    public InvalidCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
