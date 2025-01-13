package com.local.persistence;

import com.local.entity.Payment;

public interface PaymentDAO {
    Payment addPayment(Payment payment) throws DAOException;

    Payment getPendingPayment(int userId) throws DAOException;

    void updatePayment(Payment payment) throws DAOException;
}
