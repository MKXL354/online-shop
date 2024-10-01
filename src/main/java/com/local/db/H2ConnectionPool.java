package com.local.db;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class H2ConnectionPool extends ConnectionPool {
    private JdbcConnectionPool connectionPool;
    private final int retryCount = 5;
    private final int retryDelay = 500;

    public H2ConnectionPool(DatabaseConfig config) {
        super(config);
    }

    @Override
    public void openPool() {
        String url = config.getUrl();
        String username = config.getUsername();
        String password = config.getPassword();
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
                        Thread.sleep(retryDelay);
                    }
                    catch(InterruptedException ie){
                        Thread.currentThread().interrupt();
                    }
                }
                else{
                    throw new DataBaseConnectionException("Failed to get connection", e);
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
