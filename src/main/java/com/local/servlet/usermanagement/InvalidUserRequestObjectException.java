package com.local.servlet.usermanagement;

import com.local.commonexceptions.ApplicationException;

public class InvalidUserRequestObjectException extends ApplicationException {
    public InvalidUserRequestObjectException(String message, Throwable cause) {
        super(message, cause);
    }
}
