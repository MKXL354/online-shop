package com.local.persistence;

import com.local.exception.common.ApplicationException;

public class DAOException extends ApplicationException {
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
