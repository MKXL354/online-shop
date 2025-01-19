package com.local.web.controller;

import com.local.dto.ErrorResponse;
import com.local.dto.ErrorResponseMapper;
import com.local.util.logging.LogLevel;
import com.local.util.logging.LogManager;
import com.local.util.logging.LogObject;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RequestExceptionHandler {
    private LogManager logManager;
    private ErrorResponseMapper errorResponseMapper;

    @Autowired
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Autowired
    public void setErrorResponseMapper(ErrorResponseMapper errorResponseMapper) {
        this.errorResponseMapper = errorResponseMapper;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e, HttpServletRequest request) {
        ErrorResponse errorResponse = errorResponseMapper.createErrorResponse(e);
        if(errorResponse == null){
            logManager.submit(new LogObject.Builder().setClientIp(request.getRemoteAddr()).setUrl(request.getRequestURL().toString()).setCode(500).setLevel(LogLevel.ERROR).setThrowable(e).setEndTime(LocalDateTime.now()).build());
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.status(errorResponse.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            String fieldMessage = String.format("%s: %s(rejected): %s", fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage());
            message.append(fieldMessage).append("\n");
        });
        return ResponseEntity.status(400).body(new ErrorResponse(400, "NotValidException", message.toString()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException() {
        return ResponseEntity.status(400).body(new ErrorResponse(400, "MessageNotReadableException", "message/json is malformed"));
    }
}
