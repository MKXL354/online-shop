package com.local.util.logging;

import com.local.commonexceptions.ApplicationRuntimeException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Deque;
import java.util.concurrent.*;

public class BatchLogManager {
    private String outputDirectory;
    private int maxWaitTimeMillis;
    private int maxLogsToWrite;
    private ScheduledExecutorService executorService;
    private Deque<BaseLog> logs;

    public BatchLogManager(String outputDirectory, int maxWaitTimeMillis, int maxLogsToWrite) {
        this.outputDirectory = outputDirectory;
        createDirectory();
        this.maxWaitTimeMillis = maxWaitTimeMillis;
        this.maxLogsToWrite = maxLogsToWrite;
        this.logs = new ConcurrentLinkedDeque<>();
    }

    private void createDirectory(){
        Path path = Path.of(outputDirectory);
        if(!Files.isDirectory(path)){
            try{
                Files.createDirectory(path);
            }
            catch(IOException e){
                throw new ApplicationRuntimeException("cannot create logging directory" + e.getMessage(), e);
            }
        }
    }

    public void addLog(BaseLog log){
        logs.addLast(log);
    }

    public void start(){
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::log, 0, maxWaitTimeMillis, TimeUnit.MILLISECONDS);
    }

    public void shutDown(){
        this.maxLogsToWrite = logs.size();
        log();
        executorService.shutdown();
    }

    private void log() {
        if(logs.isEmpty()){
            return;
        }
        StringBuilder logContents = new StringBuilder();
        BaseLog log;
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
//TODO: maybe make this abstract (causes problem with polymorphism and method naming)
//TODO: maybe add logAll() & log when the number of logs reach a limit (causes blocking issues in high load)
