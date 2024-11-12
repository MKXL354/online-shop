package com.local.exception.service.payment;

import com.local.exception.service.ServiceException;

public class WebPaymentException extends ServiceException {
    public WebPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
