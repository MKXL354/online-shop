package com.local.service.scheduler;

import java.util.List;
import java.util.concurrent.*;

public class TaskScheduler {
    private int poolSize;
    private ScheduledExecutorService scheduler;

    public TaskScheduler(int poolSize) {
        this.poolSize = poolSize;
    }

    public void start(){
        this.scheduler = Executors.newScheduledThreadPool(poolSize);
    }

    public void stop(){
        List<Runnable> remainingTasks = scheduler.shutdownNow();
        try(ExecutorService executorService = Executors.newCachedThreadPool();){
            remainingTasks.forEach(executorService::execute);
        }
    }

    public void submitTask(Callable<?> task, int delayMillis){
        scheduler.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
    }
}
//TODO: log for exceptions happening in the tasks? (after rewriting log management)
//TODO: maybe add pre-mature cancellation(if succeeded in services)
