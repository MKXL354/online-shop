package com.local.util.transaction;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TransactionProxy implements InvocationHandler {
    private final Object target;
    private final TransactionManager transactionManager;

    public TransactionProxy(Object target) {
        this.target = target;
        this.transactionManager = TransactionManager.getInstance();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isAnnotationPresent(ManagedTransaction.class)) {
            for (int i = 0; i < 5; i++) {
                try {
                    transactionManager.startTransaction();
                    Object result = method.invoke(target, args);
                    transactionManager.commitTransaction();
                    return result;
                }
                catch (InvocationTargetException e) {
                    transactionManager.rollbackTransaction();
                    Throwable throwable = e.getCause();
                    if(!(throwable instanceof TransactionException)) {
                        throw throwable;
                    }
                }
            }
            throw new TransactionException("try again later", null);
        }
        else {
            return method.invoke(target, args);
        }
    }
}
