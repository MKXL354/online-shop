package com.local.config.manual;

import com.local.util.logging.LogManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LogManagerConfig {
    private LogManager logManager;
    private String outputDirectory;
    private int maxLogsToWrite;
    private long maxWaitTimeMillis;

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
    public void init() {
        logManager = LogManager.getInstance();
        logManager.setMaxLogsToWrite(maxLogsToWrite);
        logManager.setOutputDirectory(outputDirectory);
        logManager.setMaxWaitTimeMillis(maxWaitTimeMillis);
        logManager.start();
    }

    @PreDestroy
    public void destroy() {
        logManager.stop();
    }
}
//TODO: redo this maybe with @Bean
