package com.local.exception.service.payment;

import com.local.exception.service.ServiceException;

public class PaymentServiceException extends ServiceException {
    public PaymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
