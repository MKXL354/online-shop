//package com.local.dao.transaction;
//
//import java.lang.reflect.InvocationHandler;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//public class MemTransactionProxy implements InvocationHandler {
//    private final Object target;
//    private final TransactionManager transactionManager;
//
//    public MemTransactionProxy(Object target) {
//        this.target = target;
//        this.transactionManager = TransactionManager.getInstance();
//    }
//
//    @Override
//    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        if (method.isAnnotationPresent(ManagedTransaction.class)) {
//            for (int i = 0; i < 5; i++) {
//                try {
//                    transactionManager.startTransaction();
//                    Object result = method.invoke(target, args);
//                    transactionManager.commitTransaction();
//                    return result;
//                }
//                catch (InvocationTargetException e) {
//                    transactionManager.rollbackTransaction();
//                    Throwable throwable = e.getCause();
//                    if(!(throwable instanceof BadTransactionException)) {
//                        throw throwable;
//                    }
//                }
//            }
//            throw new BadTransactionException("try again later", null);
//        }
//        else {
//            return method.invoke(target, args);
//        }
//    }
//}
