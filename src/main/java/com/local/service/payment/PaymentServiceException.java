package com.local.service.payment;

import com.local.service.ServiceException;

public class PaymentServiceException extends ServiceException {
    public PaymentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
