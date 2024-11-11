package com.local.util.logging;

public class ActivityLog extends BaseLog {
    private String clientIp;
    private String url;

    public ActivityLog(String clientIp, String url) {
        super();
        this.clientIp = clientIp;
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format("[ip: %s][endpoint: %s]", clientIp, url) + super.toString();
    }
}
