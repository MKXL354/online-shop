package com.local.util.transaction;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Transaction<K, V> {
    private final List<Lock> locks;
    private final LockManager lockManager;
    private final Map<K, V> createdResource;
    private final Map<K, V> updatedResource;
    private final Map<K, V> deletedResource;
    private final Map<K, V> data;

    public Transaction(Map<K, V> data) {
        this.lockManager = LockManager.getInstance();
        locks = new LinkedList<>();
        createdResource = new HashMap<>();
        updatedResource = new HashMap<>();
        deletedResource = new HashMap<>();
        this.data = data;
    }

    public void addCreated(K key, V value) {
        createdResource.put(key, value);
    }

    public void addUpdated(K key, V value) {
        updatedResource.put(key, value);
    }

    public void addDeleted(K key, V value) {
        deletedResource.put(key, value);
    }

    public void lockResource(Class<?> objectClass, Object id) throws TransactionException {
        Lock lock = lockManager.getLock(objectClass, id);
        try {
            if(lock.tryLock(ThreadLocalRandom.current().nextInt(10, 20), TimeUnit.MILLISECONDS)){
                locks.add(lock);
            }
            else{
                rollbackTransaction();
                Thread.sleep(ThreadLocalRandom.current().nextInt(60, 100));
                throw new TransactionException("failed transaction", null);
            }
        } catch (InterruptedException e) {
            unlockAllLocks();
            throw new TransactionException("failed transaction", null);
        }
    }

    public void rollbackTransaction() {
        for(K createdKey : createdResource.keySet()){
            data.remove(createdKey);
        }
        createdResource.clear();

        for(K updatedKey : updatedResource.keySet()){
            data.put(updatedKey, updatedResource.get(updatedKey));
        }
        updatedResource.clear();

        for(K deletedKey : deletedResource.keySet()){
            data.put(deletedKey, deletedResource.get(deletedKey));
        }
        deletedResource.clear();

        unlockAllLocks();
    }

    public void unlockAllLocks() {
        for(Lock lock : locks){
            lock.unlock();
        }
        locks.clear();
    }
}
