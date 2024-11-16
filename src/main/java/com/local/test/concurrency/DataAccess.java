package com.local.test.concurrency;

import com.local.util.lock.LockManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DataAccess {
    private ConcurrentMap<Integer, Datum> data;
    private ConcurrentMap<Thread, Transaction> transactions;
    private LockManager lockManager;
    private Lock dataAccessLock;

    public DataAccess(LockManager lockManager) {
        this.data = new ConcurrentHashMap<>();
        data.put(1, new Datum(1, 1));
        data.put(2, new Datum(2, 2));
        this.transactions = new ConcurrentHashMap<>();
        this.lockManager = lockManager;
        this.dataAccessLock = new ReentrantLock();
    }

    public void startTransaction() {
        transactions.put(Thread.currentThread(), new Transaction(lockManager));
        dataAccessLock.lock();
    }

    public void endTransaction() {
        transactions.get(Thread.currentThread()).unlockResources();
        transactions.remove(Thread.currentThread());
        dataAccessLock.unlock();
    }

    private void lockResource(Class<?> objectClass, Object id) {
        Transaction transaction = transactions.get(Thread.currentThread());
        if(transaction != null) {
            transaction.lockResource(objectClass, id);
        }
    }

    public Datum getDatum(int id) {
        lockResource(Datum.class, id);
        System.out.println("get " + id);
        return new Datum(data.get(id));
    }

    public void updateDatum(Datum datum) {
//        maybe a lock acquisition is needed here as well
        System.out.println("set " + datum.getValue());
        data.put(datum.getId(), datum);
    }
}
//TODO: read the proposed solution
