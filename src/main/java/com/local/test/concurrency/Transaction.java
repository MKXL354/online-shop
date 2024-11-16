package com.local.test.concurrency;

import com.local.util.lock.LockManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class Transaction {
    private List<Lock> locks;
    private LockManager lockManager;

    public Transaction(LockManager lockManager) {
        this.locks = new ArrayList<>();
        this.lockManager = lockManager;
    }

    public void lockResource(Class<?> objectClass, Object id){
        Lock lock = lockManager.getLock(objectClass, id);
        locks.add(lock);
        lock.lock();
    }

    public void unlockResources(){
        for(Lock lock : locks){
            lock.unlock();
        }
    }
}
