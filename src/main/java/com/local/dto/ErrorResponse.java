package com.local.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ErrorResponse implements Serializable {
    private String timestamp;
    private int statusCode;
    private String errorResponseType;
    private String message;

    public ErrorResponse(int statusCode, String errorResponseType, String message) {
        this.timestamp = LocalDateTime.now().toString();
        this.statusCode = statusCode;
        this.errorResponseType = errorResponseType;
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorResponseType() {
        return errorResponseType;
    }

    public String getMessage() {
        return message;
    }
}
