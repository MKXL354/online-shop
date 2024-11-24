package com.local.util.persistence;

public interface PersistenceManager {
    void persistData(Object object);
    <T> T loadData(Class<T> type);
}
