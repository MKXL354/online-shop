package com.local.dao;

import com.local.commonexceptions.ApplicationException;

public class DataAccessException extends ApplicationException {
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
