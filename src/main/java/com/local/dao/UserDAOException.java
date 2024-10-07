package com.local.dao;

import com.local.commonexceptions.ApplicationException;

public class UserDAOException extends ApplicationException {
    public UserDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
