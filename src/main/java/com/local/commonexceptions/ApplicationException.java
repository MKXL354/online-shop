package com.local.commonexceptions;

public class ApplicationException extends Exception{
    public ApplicationException(String message, Throwable cause){
        super(message, cause);
    }
}
