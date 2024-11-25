package com.local.service.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class RollbackScheduler {
    protected int waitBetweenRollbacksMillis;
    protected int waitBeforeRollbackMillis;
    protected ScheduledExecutorService scheduler;

    public RollbackScheduler(int waitBetweenRollbacksMillis, int waitBeforeRollbackMillis) {
        this.waitBetweenRollbacksMillis = waitBetweenRollbacksMillis;
        this.waitBeforeRollbackMillis = waitBeforeRollbackMillis;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

    public void start(){
        scheduler.scheduleWithFixedDelay(this::rollback, 0, waitBetweenRollbacksMillis, TimeUnit.MILLISECONDS);
    }

    public void stop(){
        scheduler.shutdownNow();
        rollback();
    }

    protected abstract void rollback();
}
