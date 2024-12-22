package com.local.dao.transaction.db;

import com.local.dao.db.dbconnector.ConnectionPool;
import com.local.dao.db.dbconnector.DataBaseConnectionException;
import com.local.dao.transaction.BadTransactionException;
import com.local.dao.transaction.TransactionManager;
import com.local.dao.transaction.TransactionManagerException;
import com.local.util.logging.LogLevel;
import com.local.util.logging.LogObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DBTransactionManager implements TransactionManager {
    private ConnectionPool connectionPool;
    private ConcurrentHashMap<Thread, Connection> threadConnections = new ConcurrentHashMap<>();

    @Autowired
    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    @Override
    public void startTransaction() throws TransactionManagerException {
        if(threadConnections.containsKey(Thread.currentThread())) {
            throw new BadTransactionException("previous transaction is not closed", null);
        }
        try{
            Connection connection = connectionPool.getConnection();
            connection.setAutoCommit(false);
            connection.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            threadConnections.put(Thread.currentThread(), connection);
        }
        catch(SQLException | DataBaseConnectionException e){
            throw new TransactionManagerException(e.getMessage(), e);
        }
    }

    @Override
    public void rollbackTransaction() throws TransactionManagerException {
        try{
            Connection connection = removeConnection();
            connection.rollback();
            resetConnection(connection);
        }
        catch(SQLException e){
            throw new TransactionManagerException(e.getMessage(), e);
        }
    }

    @Override
    public void commitTransaction() throws TransactionManagerException {
        try{
            Connection connection = removeConnection();
            connection.commit();
            resetConnection(connection);
        }
        catch(SQLException e){
            throw new TransactionManagerException(e.getMessage(), e);
        }
    }

    private Connection removeConnection(){
        if (!threadConnections.containsKey(Thread.currentThread())) {
            throw new BadTransactionException("no connection is opened", null);
        }
        return threadConnections.remove(Thread.currentThread());
    }

    private void resetConnection(Connection connection) throws SQLException {
        connection.setAutoCommit(true);
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.close();
    }

    @Override
    public Connection openConnection() throws TransactionManagerException {
        try{
            Connection connection = threadConnections.get(Thread.currentThread());
            if(connection == null){
                return connectionPool.getConnection();
            }
            return connection;
        }
        catch(DataBaseConnectionException e){
            throw new TransactionManagerException(e.getMessage(), e);
        }
    }

    @Override
    public void closeConnection(Connection connection) {
        try{
            if(!threadConnections.contains(connection)){
                connection.close();
            }
        }
        catch(SQLException e){
            new LogObject.Builder().setLevel(LogLevel.ERROR).setThrowable(e).setEndTime(LocalDateTime.now()).build().submit();
        }
    }
}

