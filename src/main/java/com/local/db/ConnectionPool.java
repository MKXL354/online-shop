package com.local.db;

import java.sql.Connection;

public abstract class ConnectionPool {
    protected DatabaseConfig config;

    public ConnectionPool(DatabaseConfig config){
        this.config = config;
    }

    public abstract void openPool();
    public abstract Connection getConnection() throws DataBaseConnectionException;
    public abstract void closePool();
}
