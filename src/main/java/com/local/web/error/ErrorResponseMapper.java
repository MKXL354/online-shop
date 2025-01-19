package com.local.web.error;

import java.util.concurrent.ConcurrentHashMap;

public class ErrorResponseMapper {
    private ConcurrentHashMap<String, Integer> errorResponseMap = new ConcurrentHashMap<>();

    public void addErrorMapping(String errorResponseType, Integer errorResponseCode) {
        errorResponseMap.put(errorResponseType, errorResponseCode);
    }

    public ErrorResponse createErrorResponse(Throwable throwable) {
        String errorResponseType = throwable.getClass().getSimpleName();
        Integer statusCode = errorResponseMap.get(errorResponseType);
        if (statusCode == null) {
            Throwable cause = throwable.getCause();
            if(cause == null) {
                return null;
            }
            return createErrorResponse(cause);
        }
        String message = throwable.getMessage();
        return new ErrorResponse(statusCode, errorResponseType, message);
    }
}
//TODO: maybe move this to util
