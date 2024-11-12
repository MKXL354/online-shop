package com.local.exception.common;

public class ApplicationException extends Exception{
    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }
}
