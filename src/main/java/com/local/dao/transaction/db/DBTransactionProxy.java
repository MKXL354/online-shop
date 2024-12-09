package com.local.dao.transaction.db;

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
                    if(causedBy(e, SQLTransientException.class)){
                        ThreadLocalRandom.current().nextInt(50, 250);
                        continue;
                    }
                    throw e.getCause();
                }
            }
            throw new ApplicationRuntimeException("transaction failed", null);
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

    private boolean causedBy(Throwable main, Class<?> cause) {
        while((main = main.getCause()) != null){
            if(cause.isInstance(main)){
                return true;
            }
        }
        return false;
    }
}
