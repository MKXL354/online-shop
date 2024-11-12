package com.local.dbconnector;

import com.local.util.PropertyManager;

import java.sql.Connection;

public abstract class ConnectionPool {
    protected PropertyManager propertyManager;

    public ConnectionPool(PropertyManager propertyManager){
        this.propertyManager = propertyManager;
    }

    public abstract void openPool();
    public abstract Connection getConnection() throws DataBaseConnectionException;
    public abstract void closePool();
}
