package com.local.dao.transaction.db;

import com.local.dao.transaction.ManagedTransaction;
import com.local.dao.transaction.TransactionManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DBTransactionProxy implements InvocationHandler {
    private final Object target;
    private final TransactionManager transactionManager;

    public DBTransactionProxy(Object target, TransactionManager transactionManager) {
        this.target = target;
        this.transactionManager = transactionManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.isAnnotationPresent(ManagedTransaction.class)) {
            try {
                transactionManager.startTransaction();
                Object result = method.invoke(target, args);
                transactionManager.commitTransaction();
                return result;
            }
            catch(InvocationTargetException e) {
                transactionManager.rollbackTransaction();
                throw e.getCause();
            }
        }
        else {
            return method.invoke(target, args);
        }
    }
}
