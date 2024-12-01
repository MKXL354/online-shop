package com.local.dao.dbconnector;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.util.PropertyManager;
import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class H2ConnectionPool extends ConnectionPool {
    private JdbcConnectionPool connectionPool;
    private int retryCount;
    private int retryDelayMillis;

    public H2ConnectionPool(PropertyManager propertyManager, int retryCount, int retryDelayMillis) {
        super(propertyManager);
        this.retryCount = retryCount;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public void openPool() {
        String url = propertyManager.getProperty("url");
        String username = propertyManager.getProperty("password");
        String password = propertyManager.getProperty("password");
        if(url == null || username == null || password == null) {
            throw new ApplicationRuntimeException("bad config file format", null);
        }
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

    @Override
    public void closePool() {
        connectionPool.dispose();
    }
}
