package com.local.util;

import com.local.commonexceptions.ApplicationRuntimeException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class BaseLogger {
    protected String outputDirectory;
    protected LocalDateTime requestTime;
    protected LocalDateTime responseTime;
    protected LogLevel level;
    protected String message;

    public BaseLogger(String outputDirectory) {
        this.requestTime = LocalDateTime.now();
        this.outputDirectory = outputDirectory;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    protected abstract String getLog();

    public void log(){
        if(level == null){
            throw new ApplicationRuntimeException("log level must be set beforehand", null);
        }
        responseTime = LocalDateTime.now();
        LocalDate date = responseTime.toLocalDate();
        String log = getLog();
//        TODO: modern NIO, Async writing, log object and logger util, batch logging
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputDirectory+date.toString()+".txt", true))) {
            writer.write(log + "\n");
        }
        catch(IOException e){
            System.out.println("logging failed "+ e.getMessage());
            System.out.println(log);
        }
    }

    @Override
    public String toString() {
        return String.format("[%s][%s][%s] -%s]", requestTime.toString(), responseTime.toString(), level.toString(), message);
    }
}
//TODO: use file separator not '/'
//TODO: fix the output path to logs/
