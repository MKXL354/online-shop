package com.local.dao.payment;

import com.local.model.Cart;
import com.local.model.Payment;
import com.local.model.PaymentStatus;
import com.local.model.User;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PaymentDAOMemImpl implements PaymentDAO {
    private ConcurrentHashMap<Integer, Payment> payments;
    private AtomicInteger autoGeneratedId;

    public PaymentDAOMemImpl() {
        payments = new ConcurrentHashMap<>();
        autoGeneratedId = new AtomicInteger(1);
    }

    @Override
    public Payment addPayment(Payment payment) {
        int id = autoGeneratedId.getAndIncrement();
        payment.setId(id);
        payments.put(id, payment);
        return new Payment(payment);
    }

    @Override
    public Payment getPendingPayment(User user) {
        Payment payment = payments.searchValues(16, (p) -> p.getUser().getId() == user.getId() && p.getStatus() == PaymentStatus.PENDING ? p : null);
        if(payment != null) {
            return new Payment(payment);
        }
        return null;
    }

    @Override
    public HashSet<Payment> getAllPayments() {
        HashSet<Payment> newPayments = new HashSet<>();
        for(Payment payment : payments.values()){
            newPayments.add(new Payment(payment));
        }
        return newPayments;
    }

    @Override
    public HashSet<Payment> getAllPendingPayments() {
        HashSet<Payment> pendingPayments = new HashSet<>();
        for(Payment payment : payments.values()) {
            if(payment.getStatus() == PaymentStatus.PENDING) {
                pendingPayments.add(new Payment(payment));
            }
        }
        return pendingPayments;
    }

    @Override
    public void updatePayment(Payment payment) {
        payments.put(payment.getId(), payment);
    }
}
