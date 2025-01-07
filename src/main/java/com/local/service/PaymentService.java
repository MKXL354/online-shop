package com.local.service;

import com.local.persistence.DAOException;
import com.local.persistence.PaymentDAO;
import com.local.persistence.UserDAO;
import com.local.persistence.transaction.ManagedTransaction;
import com.local.exception.service.payment.InsufficientBalanceException;
import com.local.exception.service.payment.PaymentInProgressException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.payment.WebPaymentException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Payment;
import com.local.model.PaymentStatus;
import com.local.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {
    private CommonService commonService;
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private ConcurrentHashMap<Integer, Payment> inProgressPayments = new ConcurrentHashMap<>();

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @ManagedTransaction
    public void addBalance(int userId, BigDecimal amount) throws UserNotFoundException, DAOException {
        User user = commonService.getUserById(userId);
        user.setBalance(user.getBalance().add(amount));
        userDAO.updateUser(user);
    }

    @ManagedTransaction
    public void balancePay(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, PaymentInProgressException, InsufficientBalanceException, DAOException {
        User user = commonService.getUserById(userId);
        Payment payment = commonService.getPendingPayment(userId);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no pending payment found", null);
        }
        if(user.getBalance().compareTo(payment.getAmount()) < 0){
            throw new InsufficientBalanceException("insufficient account balance", null);
        }
        if(inProgressPayments.putIfAbsent(payment.getId(), payment) != null){
            throw new PaymentInProgressException("payment already in progress", null);
        }

        try{
            user.setBalance(user.getBalance().subtract(payment.getAmount()));
            userDAO.updateUser(user);

            payment.setLastUpdateTime(LocalDateTime.now());
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            paymentDAO.updatePayment(payment);
        }
        finally {
            inProgressPayments.remove(payment.getId());
        }
    }

    @ManagedTransaction
    public void cardPay(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, PaymentInProgressException, WebPaymentException, DAOException {
        Payment payment = commonService.getPendingPayment(userId);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no active payment found", null);
        }
        if(inProgressPayments.putIfAbsent(payment.getId(), payment) != null){
            throw new PaymentInProgressException("payment already in progress", null);
        }
        try{
            //Simulated connection to foreign API (like a payment portal)
            Thread.sleep(2*60*1000);
            double randomValue = ThreadLocalRandom.current().nextDouble(0, 1);
            if(randomValue < 0.7){
                payment.setLastUpdateTime(LocalDateTime.now());
                payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
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
        finally {
            inProgressPayments.remove(payment.getId());
        }
    }
}
//TODO: payment cache upper in the layers?
