package com.local.service.user;

public class PreviousPaymentPendingException extends UserServiceException{
    public PreviousPaymentPendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
