package com.local.service.scheduler;

import java.util.Set;
import java.util.concurrent.*;

public class TaskScheduler {
    private int poolSize;
    private ScheduledExecutorService scheduler;
    private Set<Callable<?>> remainingTasks;

    public TaskScheduler(int poolSize) {
        this.poolSize = poolSize;
        this.remainingTasks = ConcurrentHashMap.newKeySet();
    }

    public void start(){
        this.scheduler = Executors.newScheduledThreadPool(poolSize);
    }

    public void stop(){
        scheduler.shutdownNow();
        ExecutorService executorService = Executors.newCachedThreadPool();
        remainingTasks.forEach(executorService::submit);
        executorService.shutdown();
        try {
            executorService.awaitTermination(24, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    public void submitTask(Callable<?> task, int delayMillis){
        remainingTasks.add(task);
        scheduler.schedule(() -> {
            try {
                task.call();
                remainingTasks.remove(task);
            } catch (Exception e) {}
        }, delayMillis, TimeUnit.MILLISECONDS);
    }
}
//TODO: log for exceptions happening in the tasks? (after rewriting log management)
//TODO: maybe add pre-mature cancellation(if succeeded in services)