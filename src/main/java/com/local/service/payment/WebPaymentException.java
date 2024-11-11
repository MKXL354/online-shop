package com.local.service.payment;

import com.local.service.ServiceException;

public class WebPaymentException extends ServiceException {
    public WebPaymentException(String message, Throwable cause) {
        super(message, cause);
    }
}
