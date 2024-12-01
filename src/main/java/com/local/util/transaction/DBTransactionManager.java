package com.local.util.transaction;

import com.local.dao.DAOException;
import com.local.dao.dbconnector.ConnectionPool;
import com.local.dao.dbconnector.DataBaseConnectionException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

public class DBTransactionManager {
    private ConnectionPool connectionPool;
    private ConcurrentHashMap<Thread, Connection> threadConnections = new ConcurrentHashMap<>();

    public void startTransaction() throws DAOException {
        if(threadConnections.containsKey(Thread.currentThread())) {
            throw new TransactionException("previous transaction is not closed", null);
        }
        try {
            Connection connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            threadConnections.put(Thread.currentThread(), connectionPool.getConnection());
        } catch (SQLException | DataBaseConnectionException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    public void rollbackTransaction() throws DAOException {
        try {
            Connection connection = resetTransaction();
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    public void commitTransaction() throws DAOException {
        try {
            Connection connection = resetTransaction();
            connection.commit();
            connection.close();
        } catch (SQLException e) {
            throw new DAOException(e.getMessage(), e);
        }
    }

    private Connection resetTransaction() throws SQLException {
        if (threadConnections.get(Thread.currentThread()) == null) {
            throw new TransactionException("no connection is opened", null);
        }
        Connection connection = threadConnections.remove(Thread.currentThread());
        connection.setAutoCommit(true);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        return connection;
    }

    public Connection getConnection() throws DataBaseConnectionException {
        Connection connection = threadConnections.get(Thread.currentThread());
        if(connection == null){
            return connectionPool.getConnection();
        }
        return connection;
    }
}
