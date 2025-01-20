package com.local.scheduler;

import com.local.util.logging.LogLevel;
import com.local.util.logging.LogManager;
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
    private LogManager logManager;
    private int poolSize;
    private ScheduledExecutorService scheduler;
    private Set<Callable<?>> remainingTasks = ConcurrentHashMap.newKeySet();

    @Autowired
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

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
        remainingTasks.forEach((task) -> executorService.submit(() -> {
            try {
                task.call();
            } catch (Exception e) {
                logManager.submit(new LogObject.Builder().setLevel(LogLevel.ERROR).setThrowable(e).setEndTime(LocalDateTime.now()).build());
            }

        }));
        executorService.shutdown();
        try {
            if(!executorService.awaitTermination(15, TimeUnit.MINUTES)){
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        executorService.close();
    }

    public void submitTask(Callable<?> task, long delayMillis){
        remainingTasks.add(task);
        scheduler.schedule(() -> {
            try {
                remainingTasks.remove(task);
                task.call();
            } catch (Exception e) {
                remainingTasks.add(task);
                logManager.submit(new LogObject.Builder().setLevel(LogLevel.ERROR).setThrowable(e).setEndTime(LocalDateTime.now()).build());
            }
        }, delayMillis, TimeUnit.MILLISECONDS);
    }
}