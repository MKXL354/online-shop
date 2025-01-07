package com.local.persistence.transaction;

import java.sql.Connection;

public interface TransactionManager {
    void startTransaction() throws TransactionManagerException;
    void rollbackTransaction() throws TransactionManagerException;
    void commitTransaction() throws TransactionManagerException;
    Connection openConnection() throws TransactionManagerException;
    void closeConnection(Connection connection);
}
