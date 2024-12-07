package com.local.dao;

import com.local.model.Payment;

public interface PaymentDAO {
    Payment addPayment(Payment payment) throws DAOException;
    Payment getPendingPayment(int userId) throws DAOException;
    void updatePayment(Payment payment) throws DAOException;
}
