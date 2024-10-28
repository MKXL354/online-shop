package com.local.service.payment;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.user.UserDAO;
import com.local.model.Payment;
import com.local.model.User;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class PaymentService {
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private ConcurrentHashMap<String, ReentrantLock> userLocks;

    public PaymentService(UserDAO userDAO, PaymentDAO paymentDAO) {
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
        this.userLocks = new ConcurrentHashMap<>();
    }

    public void addBalance(User user, double amount) throws DAOException {
        ReentrantLock lock = userLocks.computeIfAbsent(user.getUsername(), (u) -> new ReentrantLock());
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

//TODO: multiple payments for the same user at the same time?
