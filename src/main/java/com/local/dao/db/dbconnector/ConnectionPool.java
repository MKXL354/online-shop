package com.local.dao.db.dbconnector;

import java.sql.Connection;

public interface ConnectionPool {
    void openPool();
    Connection getConnection() throws DataBaseConnectionException;
    void closePool();
}
