package com.local.dao.dbconnector;

import com.local.exception.common.ApplicationException;

public class DataBaseConnectionException extends ApplicationException {
    public DataBaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
