package com.local.service.user;

import com.local.exception.common.ApplicationRuntimeException;
import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.model.Payment;
import com.local.exception.service.TransactionException;
import com.local.util.lock.LockManager;
import com.local.util.logging.ActivityLog;
import com.local.util.logging.BatchLogManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class PaymentRollbackService {
    private PaymentDAO paymentDAO;
    private LockManager lockManager;
    private BatchLogManager batchLogManager;
    private int waitBeforeRollbackMillis;
    private int waitBetweenRollbacksMillis;
    private BlockingQueue<Runnable> paymentRollbacksQueue = new LinkedBlockingQueue<>();
    private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public PaymentRollbackService(PaymentDAO paymentDAO, LockManager lockManager, BatchLogManager batchLogManager, int waitBeforeRollbackMillis, int waitBetweenRollbacksMillis) {
        this.paymentDAO = paymentDAO;
        this.lockManager = lockManager;
        this.batchLogManager = batchLogManager;
        this.waitBeforeRollbackMillis = waitBeforeRollbackMillis;
        this.waitBetweenRollbacksMillis = waitBetweenRollbacksMillis;
    }

    private void automaticPaymentRollBack(Consumer<Payment> paymentConsumer){
        TreeSet<Payment> sortedPayments;
        try {
            sortedPayments = new TreeSet<>(paymentDAO.getAllPendingPayments());
        } catch (DAOException e) {
            ActivityLog log = new ActivityLog("local", "local");
            log.createExceptionLog(e);
            batchLogManager.addLog(log);
            throw new ApplicationRuntimeException("skipped task", e);
        }
        LinkedList<ReentrantLock> paymentsLocks = new LinkedList<>();
        for(Payment payment : sortedPayments){
            ReentrantLock paymentLock = lockManager.getLock(Payment.class, payment.getId());
            paymentsLocks.add(paymentLock);
        }

        try{
            HashSet<Payment> updatedPayments = new HashSet<>();
            Duration duration;
            for(Payment payment : sortedPayments){
                duration = Duration.between(payment.getLastUpdate(), LocalDateTime.now());
                if(duration.toMillis() > waitBeforeRollbackMillis){
                    paymentConsumer.accept(payment);
                    updatedPayments.add(payment);
                }
            }

            try{
                for(Payment payment : updatedPayments){
                    paymentDAO.updatePayment(payment);
                }
            }
            catch(DAOException e){
                ActivityLog transactionLog = new ActivityLog("local", "local");
                transactionLog.createExceptionLog(new TransactionException("catastrophic transaction exception occurred", e));
                batchLogManager.addLog(transactionLog);
            }
        }
        finally {
            for(ReentrantLock paymentLock : paymentsLocks){
                paymentLock.unlock();
            }
        }
    }

    public void start(Consumer<Payment> paymentConsumer){
        scheduler.scheduleWithFixedDelay(() -> automaticPaymentRollBack(paymentConsumer), 0, waitBetweenRollbacksMillis, TimeUnit.MILLISECONDS);
    }

    public void shutdown(){
        scheduler.shutdown();
    }
}
//TODO: fix and completely set up the rollback mechanism
