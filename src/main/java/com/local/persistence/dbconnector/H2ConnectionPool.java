package com.local.persistence.dbconnector;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.h2.jdbcx.JdbcConnectionPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class H2ConnectionPool implements ConnectionPool {
    private JdbcConnectionPool connectionPool;
    private String url;
    private String username;
    private String password;
    private long retryCount;
    private long retryDelayMillis;

    @Autowired
    public void setUrl(@Value("${db.url}") String url) {
        this.url = url;
    }

    @Autowired
    public void setUsername(@Value("${db.username}") String username) {
        this.username = username;
    }

    @Autowired
    public void setPassword(@Value("${db.password}") String password) {
        this.password = password;
    }

    @Autowired
    public void setRetryCount(@Value("${db.retryCount}") long retryCount) {
        this.retryCount = retryCount;
    }

    @Autowired
    public void setRetryDelayMillis(@Value("${db.retryDelayMillis}") long retryDelayMillis) {
        this.retryDelayMillis = retryDelayMillis;
    }

    @PostConstruct
    @Override
    public void openPool() {
        this.connectionPool = JdbcConnectionPool.create(url, username, password);
    }

    @Override
    public Connection getConnection() throws DataBaseConnectionException {
        Connection connection = null;
        for (int i = 0; i < retryCount; i++) {
            try{
                connection = connectionPool.getConnection();
                break;
            }
            catch (SQLException e){
                if(i < retryCount - 1){
                    try{
                        Thread.sleep(retryDelayMillis);
                    }
                    catch(InterruptedException ie){
                        Thread.currentThread().interrupt();
                    }
                }
                else{
                    throw new DataBaseConnectionException("failed to get database connection", e);
                }
            }
        }
        return connection;
    }

    @PreDestroy
    @Override
    public void closePool() {
        connectionPool.dispose();
    }
}
