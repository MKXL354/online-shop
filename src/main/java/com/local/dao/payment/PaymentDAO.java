package com.local.dao.payment;

import com.local.dao.DAOException;
import com.local.model.Payment;

import java.util.HashSet;

public interface PaymentDAO {
    Payment addPayment(Payment payment) throws DAOException;
    HashSet<Payment> getAllPayments() throws DAOException;
}

//TODO: rewrite DB later
