package com.local.dao.product;

public class DuplicateProductIdException extends ProductDAOException{
    public DuplicateProductIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
