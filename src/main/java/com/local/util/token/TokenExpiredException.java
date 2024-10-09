package com.local.util.token;

import com.local.commonexceptions.ApplicationException;

public class TokenExpiredException extends ApplicationException {
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
