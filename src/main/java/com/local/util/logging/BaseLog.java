package com.local.util.logging;

import java.time.LocalDateTime;

public abstract class BaseLog {
    protected LocalDateTime requestTime;
    protected LocalDateTime responseTime;
    protected LogLevel level;
    protected String message;

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRequestTime() {
        this.requestTime = LocalDateTime.now();
    }

    public void setResponseTime() {
        this.responseTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        String displayMessage = message == null ? "" : " -" + message;
        return String.format("[startTime: %s][endTime: %s][%s]%s", requestTime.toString(), responseTime.toString(), level.toString(), displayMessage);
    }

    public void createExceptionLog(Exception exception) {
        level = LogLevel.ERROR;
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(exception).append("\n");
        for(StackTraceElement stackTraceElement : exception.getStackTrace()){
            stackTrace.append("\tat ").append(stackTraceElement.toString()).append("\n");
        }
        message = stackTrace.toString();
    }
}
