package com.local.service.user;

public class PaymentNotPendingException extends UserServiceException{
    public PaymentNotPendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
