package com.local.util;

public class ActivityLogger extends BaseLogger{
//    TODO: add invoked endpoint
    private String clientId;

    public ActivityLogger(String outputDirectory, String clientId) {
        super(outputDirectory);
        this.clientId = clientId;
    }

    @Override
    public String getLog() {
        return this.toString();
    }

    @Override
    public String toString() {
        return String.format("[%s]", clientId) + super.toString();
    }
}
