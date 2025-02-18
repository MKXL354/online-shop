package com.local.web.error;

import com.local.util.validation.ValidLocalDateTime;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class ErrorResponse {
    @NotBlank
    @ValidLocalDateTime
    private String timestamp;

    private int statusCode;

    @NotBlank
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

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorResponseType() {
        return errorResponseType;
    }

    public void setErrorResponseType(String errorResponseType) {
        this.errorResponseType = errorResponseType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
