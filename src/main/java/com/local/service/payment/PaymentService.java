package com.local.service.payment;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.user.UserDAO;
import com.local.exception.service.payment.InsufficientBalanceException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.payment.WebPaymentException;
import com.local.model.Payment;
import com.local.model.PaymentStatus;
import com.local.model.User;
import com.local.exception.service.TransactionException;
import com.local.util.lock.LockManager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class PaymentService {
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private LockManager lockManager;

    public PaymentService(UserDAO userDAO, PaymentDAO paymentDAO, LockManager lockManager) {
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
        this.lockManager = lockManager;
    }

    public Payment getPendingPayment(User user) throws DAOException {
        return paymentDAO.getPendingPayment(user);
    }

    public HashSet<Payment> getAllPayments() throws DAOException {
        return paymentDAO.getAllPayments();
    }

    public void addBalance(User user, BigDecimal amount) throws DAOException {
        ReentrantLock userLock = lockManager.getLock(User.class, user.getUsername());
        try{
            userLock.lock();
            user.setBalance(user.getBalance().add(amount));
            userDAO.updateUser(user);
        }
        finally{
            userLock.unlock();
        }
    }

    public void balancePay(User user) throws PendingPaymentNotFoundException, InsufficientBalanceException, TransactionException, DAOException {
        Payment payment = getPendingPayment(user);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no pending payment found", null);
        }
        ReentrantLock userLock = lockManager.getLock(User.class, user.getUsername());
        ReentrantLock paymentLock = lockManager.getLock(Payment.class, payment.getId());
        userLock.lock();
        paymentLock.lock();

        try{
            if(user.getBalance().compareTo(payment.getAmount()) < 0){
                throw new InsufficientBalanceException("insufficient account balance", null);
            }
            user.setBalance(user.getBalance().subtract(payment.getAmount()));
            payment.setLastUpdate(LocalDateTime.now().toString());
            payment.setStatus(PaymentStatus.SUCCESSFUL);

            try{
                userDAO.updateUser(user);
                paymentDAO.updatePayment(payment);
            }
            catch(DAOException e){
                throw new TransactionException("catastrophic transaction exception occurred", null);
            }
        }
        finally{
            userLock.unlock();
            paymentLock.unlock();
        }
    }

    public void cardPay(User user) throws PendingPaymentNotFoundException, WebPaymentException, DAOException {
        Payment payment = paymentDAO.getPendingPayment(user);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no active payment found", null);
        }
        ReentrantLock paymentLock = lockManager.getLock(Payment.class, payment.getId());
        paymentLock.lock();
        try{
            double randomValue = ThreadLocalRandom.current().nextDouble(0, 1);
            //Simulated connection to foreign API (like a payment portal)
            Thread.sleep(2*60*1000);
            if(randomValue < 0.7){
                payment.setLastUpdate(LocalDateTime.now().toString());
                payment.setStatus(PaymentStatus.SUCCESSFUL);
                paymentDAO.updatePayment(payment);
            }
            else if(randomValue < 0.9){
                throw new WebPaymentException("invalid credentials", null);
            }
            else{
                throw new WebPaymentException("not enough credit", null);
            }
        }
        catch (InterruptedException e) {
            throw new WebPaymentException("payment interrupted", null);
        } finally {
            paymentLock.unlock();
        }
    }
}
