//package com.local.dao.transaction;
//
//import com.local.exception.common.ApplicationRuntimeException;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//public class TransactionManager {
//    private ConcurrentHashMap<Thread, Transaction> threadTransactions = new ConcurrentHashMap<>();
//    private static final TransactionManager INSTANCE = new TransactionManager();
//
//    private TransactionManager() {}
//
//    public static TransactionManager getInstance() {
//        return INSTANCE;
//    }
//
//    public void startTransaction() {
//        if(isTransactionStarted()) {
//            throw new BadTransactionException("previous transaction is not closed", null);
//        }
//        threadTransactions.put(Thread.currentThread(), new Transaction());
//    }
//
//    public void rollbackTransaction() {
//        Transaction transaction = resetTransaction();
//        transaction.rollbackTransaction();
//    }
//
//    public void commitTransaction() {
//        Transaction transaction = resetTransaction();
//        transaction.unlockAllResources();
//    }
//
//    private Transaction resetTransaction() {
//        Transaction transaction = threadTransactions.get(Thread.currentThread());
//        if (transaction == null) {
//            throw new BadTransactionException("no transaction is opened", null);
//        }
//        threadTransactions.remove(Thread.currentThread());
//        return transaction;
//    }
//
//    public boolean isTransactionStarted(){
//        return threadTransactions.containsKey(Thread.currentThread());
//    }
//
//    public void lockResource(String id) throws BadTransactionException {
//        threadTransactions.get(Thread.currentThread()).lockResource(id);
//    }
//
//    public void addRestorable(Restorable restorable){
//        threadTransactions.get(Thread.currentThread()).addRestorable(restorable);
//    }
//}