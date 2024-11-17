package com.local.dto;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int statusCode;
    private final String errorResponseType;
    private final String message;

    public ErrorResponse(int statusCode, String errorResponseType, String message) {
        this.timestamp = LocalDateTime.now();
        this.statusCode = statusCode;
        this.errorResponseType = errorResponseType;
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
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
