package com.local.service.payment;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.user.UserDAO;
import com.local.model.Payment;
import com.local.model.PaymentStatus;
import com.local.model.User;
import com.local.service.TransactionException;
import com.local.util.lock.LockManager;

import java.time.LocalDateTime;
import java.util.HashSet;
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

    public Payment getActivePayment(User user) throws DAOException {
        return paymentDAO.getActivePayment(user);
    }

    public HashSet<Payment> getAllPayments() throws DAOException {
        return paymentDAO.getAllPayments();
    }

    public void addBalance(User user, double amount) throws DAOException {
        ReentrantLock userLock = lockManager.getLock(User.class, user.getUsername());
        try{
            userLock.lock();
            user.setBalance(user.getBalance() + amount);
            userDAO.updateUser(user);
        }
        finally{
            userLock.unlock();
        }
    }

    public void balancePay(User user) throws ActivePaymentNotFoundException, PaymentAlreadySucceededException, InsufficientBalanceException, TransactionException, DAOException {
        Payment payment = getActivePayment(user);
        if(payment == null){
            throw new ActivePaymentNotFoundException("no active payment found", null);
        }
        ReentrantLock userLock = lockManager.getLock(User.class, user.getUsername());
        try{
            if(payment.getStatus().equals(PaymentStatus.SUCCESSFUL)){
                throw new PaymentAlreadySucceededException("payment is already succeeded", null);
            }
            if(user.getBalance() < payment.getAmount()){
                throw new InsufficientBalanceException("insufficient account balance", null);
            }
            user.setBalance(user.getBalance() - payment.getAmount());
            payment.setLastUpdate(LocalDateTime.now());
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
        }
    }

    public void cardPay(User user){
//        TODO: card entity and chance-based validation?
    }
}
//TODO: add balance and card pay endpoints
//TODO: add order cancelling? because only one active payment is possible at any time
