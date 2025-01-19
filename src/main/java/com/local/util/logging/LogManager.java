package com.local.util.logging;

import com.local.exception.common.ApplicationRuntimeException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

@Component
public class LogManager {
    private String outputDirectory;
    private long maxWaitTimeMillis;
    private int maxLogsToWrite;
    private ScheduledExecutorService executorService;
    private Deque<LogObject> logs = new ConcurrentLinkedDeque<>();

    @Autowired
    public void setOutputDirectory(@Value("${log.output}") String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    @Autowired
    public void setMaxLogsToWrite(@Value("${log.maxLogsToWrite}") int maxLogsToWrite) {
        this.maxLogsToWrite = maxLogsToWrite;
    }

    @Autowired
    public void setMaxWaitTimeMillis(@Value("${log.maxWaitTimeMillis}") long maxWaitTimeMillis) {
        this.maxWaitTimeMillis = maxWaitTimeMillis;
    }

    @PostConstruct
    public void start(){
        Path path = Path.of(outputDirectory);
        if(!Files.isDirectory(path)){
            try{
                Files.createDirectory(path);
            }
            catch(IOException e){
                throw new ApplicationRuntimeException("cannot create logging directory " + e.getMessage(), e);
            }
        }
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.executorService.scheduleWithFixedDelay(this::log, 0, maxWaitTimeMillis, TimeUnit.MILLISECONDS);
    }

    public void submit(LogObject logObject){
        logs.addLast(logObject);
    }

    @PreDestroy
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
