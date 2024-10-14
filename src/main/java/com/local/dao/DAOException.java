package com.local.dao;

import com.local.commonexceptions.ApplicationException;

public class DAOException extends ApplicationException {
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
