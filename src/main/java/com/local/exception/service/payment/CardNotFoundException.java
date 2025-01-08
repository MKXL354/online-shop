package com.local.exception.service.payment;

public class CardNotFoundException extends PaymentServiceException{
    public CardNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
