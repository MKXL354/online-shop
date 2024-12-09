package com.local.dao.transaction.db;

import com.local.dao.DAOException;
import com.local.dao.transaction.ManagedTransaction;
import com.local.dao.transaction.TransactionManager;
import com.local.exception.common.ApplicationRuntimeException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLTransientException;
import java.util.concurrent.ThreadLocalRandom;

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
            for(int i = 0 ; i < 5; i++){
                try {
                    transactionManager.startTransaction();
                    Object result = method.invoke(target, args);
                    transactionManager.commitTransaction();
                    return result;
                }
                catch(InvocationTargetException e) {
                    transactionManager.rollbackTransaction();
                    Throwable cause = e.getCause();
//                    TODO: a better, general, recursive approach here
                    if(cause instanceof DAOException && cause.getCause() instanceof SQLTransientException){
//                        TODO: finer-tuned waiting?
                        ThreadLocalRandom.current().nextInt(50, 250);
                        continue;
                    }
                    throw cause;
                }
            }
//            TODO: or maybe throw a TransactionFailed exception
            throw new ApplicationRuntimeException("failed transaction", null);
        }
        else {
            try {
                return method.invoke(target, args);
            }
            catch(InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }
}
