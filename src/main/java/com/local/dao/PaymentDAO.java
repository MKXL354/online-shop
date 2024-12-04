package com.local.dao;

import com.local.model.Payment;
import com.local.model.User;

import java.util.HashSet;

public interface PaymentDAO {
    Payment addPayment(Payment payment) throws DAOException;
    Payment getPendingPayment(User user) throws DAOException;
    HashSet<Payment> getAllPayments() throws DAOException;
    HashSet<Payment> getAllPendingPayments() throws DAOException;
    HashSet<Payment> getAllCancelledPayments() throws DAOException;
    void updatePayment(Payment payment) throws DAOException;
}
