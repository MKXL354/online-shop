package com.local.util.logging;

import java.time.LocalDateTime;

public class LogObject {
    private LogManager logManager = LogManager.getInstance();
    private String clientIp;
    private String url;
    private LogLevel level;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Throwable throwable;

    private LogObject(Builder builder) {
        this.clientIp = builder.clientIp;
        this.url = builder.url;
        this.level = builder.level;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.throwable = builder.throwable;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUrl() {
        return url;
    }

    public LogLevel getLevel() {
        return level;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void submit(){
        LogManager.submit(this);
    }

    @Override
    public String toString() {
        String clientString = clientIp == null ? "" : "[clientIp: ]" + clientIp;
        String urlString = url == null ? "" : "[url: ]" + url;
        String startTimeString = startTime == null ? "" : "[startTime: ]" + startTime;
        String endTimeString = endTime == null ? "" : "[endTime: ]" + clientIp;
        String levelString = level == null ? "" : "[level: ]" + level;
        String throwableString = level != LogLevel.ERROR && throwable == null ? "" : "\n" + getStackTraceLog(throwable);
        return clientString + urlString + startTimeString + endTimeString + levelString + throwableString;
    }

    private String getStackTraceLog(Throwable throwable) {
        level = LogLevel.ERROR;
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(throwable.toString()).append("\n");

        Throwable cause = throwable.getCause();
        if (cause != null) {
            stackTrace.append("\tCaused by: ").append(cause).append("\n");
        }

        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            stackTrace.append("\tat ").append(stackTraceElement.toString()).append("\n");
        }

        Throwable currentCause = cause;
        while (currentCause != null) {
            for (StackTraceElement element : currentCause.getStackTrace()) {
                stackTrace.append("\tat ").append(element.toString()).append("\n");
            }
            currentCause = currentCause.getCause();
        }
        return stackTrace.toString();
    }

    public static class Builder {
        private String clientIp;
        private String url;
        private LogLevel level;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Throwable throwable;

        public Builder setClientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder setLevel(LogLevel level) {
            this.level = level;
            return this;
        }

        public Builder setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder setThrowable(Throwable throwable) {
            this.throwable = throwable;
            return this;
        }

        public LogObject build(){
            return new LogObject(this);
        }
    }
}
