package com.local.dao.user;

import com.local.commonexceptions.ApplicationException;

public class UserDAOException extends ApplicationException {
    public UserDAOException(String message, Throwable cause) {
        super(message, cause);
    }
}
