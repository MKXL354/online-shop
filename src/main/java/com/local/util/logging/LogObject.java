package com.local.util.logging;

import java.time.LocalDateTime;

public class LogObject {
    private LogManager logManager = LogManager.getInstance();
    private String clientIp;
    private String url;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LogLevel level;
    private int code;
    private Throwable throwable;

    private LogObject(Builder builder) {
        this.clientIp = builder.clientIp;
        this.url = builder.url;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.level = builder.level;
        this.code = builder.code;
        this.throwable = builder.throwable;
    }

    public String getClientIp() {
        return clientIp;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public LogLevel getLevel() {
        return level;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void submit(){
        logManager.submit(this);
    }

    @Override
    public String toString() {
        String clientString = clientIp == null ? "" : "[clientIp: " + clientIp + "]";
        String urlString = url == null ? "" : "[url: " + url + "]";
        String startTimeString = startTime == null ? "" : "[startTime: " + startTime + "]";
        String endTimeString = endTime == null ? "" : "[endTime: " + endTime + "]";
        String levelString = level == null ? "" : "[level: " + level + "]";
        String codeString = code == 0 ? null : "[code: " + code + "]";
        String throwableString = level != LogLevel.ERROR && throwable == null ? "" : "\n" + getFullStackTraceLog(throwable);
        return clientString + urlString + startTimeString + endTimeString + levelString + codeString + throwableString;
    }

    private String getFullStackTraceLog(Throwable throwable) {
        level = LogLevel.ERROR;
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(getPartialStackTraceLog(throwable));

        Throwable cause = throwable.getCause();
        while (cause != null) {
            stackTrace.append("Caused by: ").append(getPartialStackTraceLog(cause));
            cause = cause.getCause();
        }
        return stackTrace.toString();
    }

    private String getPartialStackTraceLog(Throwable throwable) {
        StringBuilder stackTrace = new StringBuilder();
        stackTrace.append(throwable.getClass().getName());
        if (throwable.getMessage() != null) {
            stackTrace.append(": ").append(throwable.getMessage());
        }
        stackTrace.append("\n");
        for (StackTraceElement stackTraceElement : throwable.getStackTrace()) {
            stackTrace.append("\tat ").append(stackTraceElement.toString()).append("\n");
        }
        return stackTrace.toString();
    }

    public static class Builder {
        private String clientIp;
        private String url;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private LogLevel level;
        private int code;
        private Throwable throwable;

        public Builder setClientIp(String clientIp) {
            this.clientIp = clientIp;
            return this;
        }

        public Builder setUrl(String url) {
            this.url = url;
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

        public Builder setLevel(LogLevel level) {
            this.level = level;
            return this;
        }

        public Builder setCode(int code) {
            this.code = code;
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
