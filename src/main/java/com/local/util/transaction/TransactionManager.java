package com.local.util.transaction;

import com.local.exception.common.ApplicationRuntimeException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class TransactionManager {
    private static final TransactionManager INSTANCE = new TransactionManager();

    private ThreadLocal<Map<Class<?>, Transaction<?, ?>>> transactions = new ThreadLocal<>();

    private TransactionManager() {}

    public static TransactionManager getInstance() {
        return INSTANCE;
    }

    public void registerDataAccess(Class<?> dataAccessClass){
//        transactions.put(dataAccessClass, )
    }

    public void startTransaction(Class<?> dataClass, Map<?, ?> data) {
//        dataMaps.put(dataClass, data);
//        threadTransaction.set(new Transaction());
//        transactions.set(new ConcurrentHashMap<>());
    }

    public void rollbackTransaction() {
        Transaction transaction = resetTransaction();
        transaction.rollbackTransaction();
    }

    public void commitTransaction() {
        Transaction transaction = resetTransaction();
        transaction.unlockAllLocks();
    }

    private Transaction resetTransaction() {
//        Transaction transaction = threadTransaction.get();
//        if (transaction == null) {
//            throw new ApplicationRuntimeException("no transaction is opened", null);
//        }
//        threadTransaction.remove();
//        dataMaps.clear();
//        return transaction;
        return null;
    }

    public void isTransactionStarted(){
//        return threadTransaction.get() != null;
    }

    public <K, V> void lockResource(Class<?> objectClass, Object id){

    }

    public void addCreated(){

    }

    public void addUpdated(){

    }

    public void addDeleted(){

    }

    public <T> void addRollBackMechanism(T obj, Consumer<T> rollbackOperation, Class<T> type){

    }
}
