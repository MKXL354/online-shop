package com.local.exception.service.payment;

public class DuplicateCardException extends PaymentServiceException{
    public DuplicateCardException(String message, Throwable cause) {
        super(message, cause);
    }
}
