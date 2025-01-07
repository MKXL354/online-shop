package com.local.aspect;

import com.local.persistence.transaction.TransactionManager;
import com.local.exception.common.ApplicationRuntimeException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLTransientException;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Aspect
public class TransactionManagementAspect {
    private TransactionManager transactionManager;

    @Autowired
    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Pointcut("@annotation(com.local.persistence.transaction.ManagedTransaction)")
    public void managedTransactionMethods() {}

    @Pointcut("within(com.local.service..*)")
    public void serviceMethods() {}

    @Around("serviceMethods() && managedTransactionMethods()")
    public Object transactionManagementAdvice(ProceedingJoinPoint pjp) throws Throwable {
        for(int i = 0 ; i < 5; i++){
            try {
                transactionManager.startTransaction();
                Object result = pjp.proceed();
                transactionManager.commitTransaction();
                return result;
            }
            catch(Throwable t) {
                transactionManager.rollbackTransaction();
                if(isInstance(t, SQLTransientException.class)){
                    ThreadLocalRandom.current().nextInt(50, 250);
                    continue;
                }
                throw t;
            }
        }
        throw new ApplicationRuntimeException("transaction failed", null);
    }

    private boolean isInstance(Throwable main, Class<?> cause) {
        do{
            if(cause.isInstance(main)){
                return true;
            }
        }while((main = main.getCause()) != null);
        return false;
    }
}
