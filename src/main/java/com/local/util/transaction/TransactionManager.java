package com.local.util.transaction;

import java.util.concurrent.ConcurrentHashMap;

public class TransactionManager {
    private ConcurrentHashMap<Thread, Transaction> threadTransactions = new ConcurrentHashMap<>();
    private static final TransactionManager INSTANCE = new TransactionManager();

    private TransactionManager() {}

    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    public void startTransaction() {
        if(isTransactionStarted()) {
            throw new RuntimeException("previous transaction is not closed", null);
        }
        threadTransactions.put(Thread.currentThread(), new Transaction());
    }

    public void rollbackTransaction() {
        Transaction transaction = resetTransaction();
        transaction.rollbackTransaction();
    }

    public void commitTransaction() {
        Transaction transaction = resetTransaction();
        transaction.unlockAllResources();
    }

    private Transaction resetTransaction() {
        Transaction transaction = threadTransactions.get(Thread.currentThread());
        if (transaction == null) {
            throw new RuntimeException("no transaction is opened", null);
        }
        threadTransactions.remove(Thread.currentThread());
        return transaction;
    }

    public boolean isTransactionStarted(){
        return threadTransactions.containsKey(Thread.currentThread());
    }

    public void lockResource(Class<?> objectClass, Object id) throws TransactionException {
        threadTransactions.get(Thread.currentThread()).lockResource(objectClass, id);
    }

    public void unlockResource(Class<?> objectClass, Object id) throws TransactionException {
        threadTransactions.get(Thread.currentThread()).unlockResource(objectClass, id);
    }

    public void addRestorable(Restorable restorable){
        threadTransactions.get(Thread.currentThread()).addRestorable(restorable);
    }
}