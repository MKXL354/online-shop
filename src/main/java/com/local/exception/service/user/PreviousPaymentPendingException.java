package com.local.exception.service.user;

public class PreviousPaymentPendingException extends UserServiceException{
    public PreviousPaymentPendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
