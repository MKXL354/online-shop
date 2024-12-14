package com.local.util.logging;

import com.local.exception.common.ApplicationRuntimeException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LogManager {
    private String outputDirectory;
    private int maxWaitTimeMillis;
    private int maxLogsToWrite;
    private ScheduledExecutorService executorService;
    private static Deque<LogObject> logs = new ConcurrentLinkedDeque<>();

    private LogManager() {}

    private static class SingletonHelper{
        private static final LogManager INSTANCE = new LogManager();
    }
    public static LogManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void start(String outputDirectory, int maxWaitTimeMillis, int maxLogsToWrite){
        this.outputDirectory = outputDirectory;
        createDirectory();
        this.maxWaitTimeMillis = maxWaitTimeMillis;
        this.maxLogsToWrite = maxLogsToWrite;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleWithFixedDelay(this::log, 0, maxWaitTimeMillis, TimeUnit.MILLISECONDS);
    }

    private void createDirectory(){
        Path path = Path.of(outputDirectory);
        if(!Files.isDirectory(path)){
            try{
                Files.createDirectory(path);
            }
            catch(IOException e){
                throw new ApplicationRuntimeException("cannot create logging directory " + e.getMessage(), e);
            }
        }
    }

    public void submit(LogObject logObject){
        logs.addLast(logObject);
    }

    public void stop(){
        executorService.shutdownNow();
        this.maxLogsToWrite = logs.size();
        log();
    }

    private void log() {
        if(logs.isEmpty()){
            return;
        }
        StringBuilder logContents = new StringBuilder();
        LogObject log;
        for(int i = 0; i < maxLogsToWrite; i++){
            if((log = logs.pollFirst()) != null) {
                logContents.append(log).append("\n");
            }
            else{
                break;
            }
        }
        LocalDate date = LocalDate.now();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory + date + ".txt", true))){
            writer.write(logContents.toString());
        }
        catch(IOException e){
            System.out.println("logging failed: "+ e.getMessage());
            System.out.println(logContents);
        }
    }
}
