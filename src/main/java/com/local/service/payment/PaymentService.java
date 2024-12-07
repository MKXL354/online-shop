package com.local.service.payment;

import com.local.dao.DAOException;
import com.local.dao.transaction.ManagedTransaction;
import com.local.exception.service.payment.InsufficientBalanceException;
import com.local.exception.service.payment.PaymentInProgressException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.payment.WebPaymentException;
import com.local.exception.service.usermanagement.UserNotFoundException;

import java.math.BigDecimal;

public interface PaymentService {
    @ManagedTransaction
    void addBalance(int userId, BigDecimal amount) throws UserNotFoundException, DAOException;
    @ManagedTransaction
    void balancePay(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, PaymentInProgressException, InsufficientBalanceException, DAOException;
    @ManagedTransaction
    void cardPay(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, WebPaymentException, DAOException;
    @ManagedTransaction
    void cancelPayment(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, DAOException;
}