package com.local.controller;

import com.local.dto.ErrorResponse;
import com.local.dto.ErrorResponseMapper;
import com.local.exception.common.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RequestExceptionHandler {
    private ErrorResponseMapper errorResponseMapper;

    @Autowired
    public void setErrorResponseMapper(ErrorResponseMapper errorResponseMapper) {
        this.errorResponseMapper = errorResponseMapper;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {
        ErrorResponse errorResponse = errorResponseMapper.createErrorResponse(e);
        if(errorResponse == null){
            return exception();
        }
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception() {
        return ResponseEntity.internalServerError().build();
    }
}
//TODO: @RequestAttribute and authorization filter
//TODO: ResponseEntity when manipulating response object is needed(setting status, adding header, etc.)
