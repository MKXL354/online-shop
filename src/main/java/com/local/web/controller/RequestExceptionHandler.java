package com.local.web.controller;

import com.local.web.dto.ErrorResponse;
import com.local.web.dto.ErrorResponseMapper;
import com.local.exception.common.ApplicationException;
import com.local.util.logging.LogLevel;
import com.local.util.logging.LogObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RequestExceptionHandler {
    private ErrorResponseMapper errorResponseMapper;

    @Autowired
    public void setErrorResponseMapper(ErrorResponseMapper errorResponseMapper) {
        this.errorResponseMapper = errorResponseMapper;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e, HttpServletRequest request) {
        ErrorResponse errorResponse = errorResponseMapper.createErrorResponse(e);
        if(errorResponse == null){
            return exception(e, request);
        }
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) {
        new LogObject.Builder().setClientIp(request.getRemoteAddr()).setUrl(request.getRequestURL().toString()).setCode(500).setLevel(LogLevel.ERROR).setThrowable(e).setEndTime(LocalDateTime.now()).build().submit();
        return ResponseEntity.internalServerError().build();
    }
}
