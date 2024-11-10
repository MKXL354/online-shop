package com.local.dao.payment;

import com.local.dao.DAOException;
import com.local.model.Payment;
import com.local.model.User;

import java.util.HashSet;

public interface PaymentDAO {
    Payment addPayment(Payment payment) throws DAOException;
    Payment getActivePayment(User user) throws DAOException;
    HashSet<Payment> getAllPayments() throws DAOException;
    void updatePayment(Payment payment) throws DAOException;
}

//TODO: rewrite DB later
