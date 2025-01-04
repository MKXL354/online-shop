package com.local.web.dto;

import java.util.concurrent.ConcurrentHashMap;

public class ErrorResponseMapper {
    private ConcurrentHashMap<String, Integer> errorResponseMap = new ConcurrentHashMap<>();

    public void addErrorMapping(String errorResponseType, Integer errorResponseCode) {
        errorResponseMap.put(errorResponseType, errorResponseCode);
    }

    public ErrorResponse createErrorResponse(Exception exception) {
        String errorResponseType = exception.getClass().getSimpleName();
        Integer statusCode = errorResponseMap.get(errorResponseType);
        if (statusCode == null) {
            return null;
        }
        String message = exception.getMessage();
        return new ErrorResponse(statusCode, errorResponseType, message);
    }
}
