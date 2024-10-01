package com.local.db;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class H2ConnectionPool extends ConnectionPool {
    private JdbcConnectionPool connectionPool;

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
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public void closePool() {
        connectionPool.dispose();
    }
}
