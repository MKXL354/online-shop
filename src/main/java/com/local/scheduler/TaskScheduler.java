package com.local.scheduler;

import com.local.util.logging.LogLevel;
import com.local.util.logging.LogObject;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.*;

@Component
public class TaskScheduler {
    private int poolSize;
    private ScheduledExecutorService scheduler;
    private Set<Callable<?>> remainingTasks = ConcurrentHashMap.newKeySet();

    @Autowired
    public void setPoolSize(@Value("${scheduler.poolSize}") int poolSize) {
        this.poolSize = poolSize;
    }

    @PostConstruct
    public void start(){
        this.scheduler = Executors.newScheduledThreadPool(poolSize);
    }

    @PreDestroy
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

    public void submitTask(Callable<?> task, long delayMillis){
        remainingTasks.add(task);
        scheduler.schedule(() -> {
            try {
                task.call();
                remainingTasks.remove(task);
            } catch (Exception e) {
                new LogObject.Builder().setLevel(LogLevel.ERROR).setThrowable(e).setEndTime(LocalDateTime.now()).build().submit();
            }
        }, delayMillis, TimeUnit.MILLISECONDS);
    }
}
//TODO: maybe add pre-mature cancellation(if succeeded in services)