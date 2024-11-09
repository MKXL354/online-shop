package com.local.service.payment;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.user.UserDAO;
import com.local.model.Payment;
import com.local.model.User;
import com.local.util.lock.LockManager;

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

    public void addBalance(User user, double amount) throws DAOException {
        ReentrantLock lock = lockManager.getLock(user.getUsername());
        try{
            lock.lock();
            user.setBalance(user.getBalance() + amount);
            userDAO.updateUser(user);
        }
        finally{
            lock.unlock();

        }
    }

    public HashSet<Payment> getAllPayments() throws DAOException {
        return paymentDAO.getAllPayments();
    }
}
