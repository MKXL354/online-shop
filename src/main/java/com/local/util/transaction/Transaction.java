package com.local.util.transaction;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Transaction {
    private final List<Lock> locks;
    private final LockManager lockManager;
    private final List<Restorable> restorables;

    public Transaction() {
        this.lockManager = LockManager.getInstance();
        this.locks = new LinkedList<>();
        this.restorables = new LinkedList<>();
    }

    public void addRestorable(Restorable restorable) {
        restorables.add(restorable);
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
        restorables.forEach(Restorable::restore);
        restorables.clear();
        unlockAllLocks();
    }

    public void unlockAllLocks() {
        for(Lock lock : locks){
            lock.unlock();
        }
        locks.clear();
    }
}
