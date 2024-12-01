package com.local.service.payment;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.user.UserDAO;
import com.local.exception.service.payment.InsufficientBalanceException;
import com.local.exception.service.payment.PaymentInProgressException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.payment.WebPaymentException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Payment;
import com.local.model.PaymentStatus;
import com.local.model.User;
import com.local.service.UtilityService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class PaymentService {
    private UtilityService utilityService;
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private Set<Payment> inProgressPayments;

    public PaymentService(UtilityService utilityService, UserDAO userDAO, PaymentDAO paymentDAO) {
        this.utilityService = utilityService;
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
        inProgressPayments = ConcurrentHashMap.newKeySet();
    }

    public Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException {
        User user = utilityService.getUserById(userId);
        return paymentDAO.getPendingPayment(user);
    }

    public HashSet<Payment> getAllPayments() throws DAOException {
        return paymentDAO.getAllPayments();
    }

    public void addBalance(int userId, BigDecimal amount) throws UserNotFoundException, DAOException {
        User user = utilityService.getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userDAO.updateUser(user);
    }

    public void balancePay(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, PaymentInProgressException, InsufficientBalanceException, DAOException {
        User user = utilityService.getUserById(userId);
        Payment payment = getPendingPayment(userId);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no pending payment found", null);
        }
        if(user.getBalance().compareTo(payment.getAmount()) < 0){
            throw new InsufficientBalanceException("insufficient account balance", null);
        }

        if(inProgressPayments.contains(payment)){
            throw new PaymentInProgressException("payment is in progress", null);
        }
        inProgressPayments.add(payment);

        try{
            user.setBalance(user.getBalance().subtract(payment.getAmount()));
            userDAO.updateUser(user);

            payment.setLastUpdate(LocalDateTime.now());
            payment.setStatus(PaymentStatus.SUCCESSFUL);
            paymentDAO.updatePayment(payment);
        }
        finally {
            inProgressPayments.remove(payment);
        }
    }

    public void cardPay(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, WebPaymentException, DAOException {
        User user = utilityService.getUserById(userId);
        Payment payment = paymentDAO.getPendingPayment(user);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no active payment found", null);
        }
        try{
            //Simulated connection to foreign API (like a payment portal)
            Thread.sleep(2*60*1000);
            double randomValue = ThreadLocalRandom.current().nextDouble(0, 1);
            if(randomValue < 0.7){
                payment.setLastUpdate(LocalDateTime.now());
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
        }
    }

    public void cancelPayment(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, DAOException {
        User user = utilityService.getUserById(userId);
        Payment payment = paymentDAO.getPendingPayment(user);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no active payment found", null);
        }
        payment.setLastUpdate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.CANCELLED);
        paymentDAO.updatePayment(payment);
    }
}
//TODO: payment cache upper in the layers
