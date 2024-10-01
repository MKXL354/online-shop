package com.local.db;

import com.local.commonexceptions.ServiceException;

public class DataBaseConnectionException extends ServiceException {
    public DataBaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
