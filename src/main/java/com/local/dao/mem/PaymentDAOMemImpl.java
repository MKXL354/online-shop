//package com.local.dao.payment;
//
//import com.local.model.Payment;
//import com.local.model.PaymentStatus;
//import com.local.model.User;
//import com.local.dao.transaction.TransactionManager;
//
//import java.io.Serial;
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.atomic.AtomicInteger;
//
//public class PaymentDAOMemImpl implements PaymentDAO, Serializable {
//    @Serial
//    private static final long serialVersionUID = -467830889288225367L;
//
//    private ConcurrentHashMap<Integer, Payment> payments;
//    private AtomicInteger autoGeneratedId;
//    private TransactionManager transactionManager;
//
//    public PaymentDAOMemImpl() {
//        payments = new ConcurrentHashMap<>();
//        autoGeneratedId = new AtomicInteger(1);
//        transactionManager = TransactionManager.getInstance();
//    }
//
//    @Override
//    public Payment addPayment(Payment payment) {
//        int id = autoGeneratedId.getAndIncrement();
//        payment.setId(id);
//        payments.putIfAbsent(id, payment);
//        return new Payment(payment);
//    }
//
//    @Override
//    public Payment getPendingPayment(User user) {
//        Payment payment = payments.searchValues(16, (p) -> p.getUser().getId() == user.getId() && p.getStatus() == PaymentStatus.PENDING ? p : null);
//        if(payment != null) {
//            if(transactionManager.isTransactionStarted()){
//                transactionManager.lockResource("" + Payment.class + payment.getId());
//            }
//            return new Payment(payment);
//        }
//        return null;
//    }
//
//    @Override
//    public HashSet<Payment> getAllPayments() {
//        HashSet<Payment> newPayments = new HashSet<>();
//        for(Payment payment : payments.values()){
//            if(transactionManager.isTransactionStarted()){
//                transactionManager.lockResource("" + Payment.class + payment.getId());
//            }
//            newPayments.add(new Payment(payment));
//        }
//        return newPayments;
//    }
//
//    @Override
//    public HashSet<Payment> getAllPendingPayments() {
//        return getPaymentsByStatus(PaymentStatus.PENDING);
//    }
//
//    @Override
//    public HashSet<Payment> getAllCancelledPayments(){
//        return getPaymentsByStatus(PaymentStatus.CANCELLED);
//    }
//
//    private HashSet<Payment> getPaymentsByStatus(PaymentStatus status) {
//        HashSet<Payment> searchedPayments = new HashSet<>();
//        for(Payment payment : payments.values()) {
//            if(payment.getStatus() == status) {
//                if(transactionManager.isTransactionStarted()){
//                    transactionManager.lockResource("" + Payment.class + payment.getId());
//                }
//                searchedPayments.add(new Payment(payment));
//            }
//        }
//        return searchedPayments;
//    }
//
//    @Override
//    public void updatePayment(Payment payment) {
//        if(transactionManager.isTransactionStarted()){
//            transactionManager.lockResource("" + Payment.class + payment.getId());
//            transactionManager.addRestorable(() -> payments.put(payment.getId(), payment));
//        }
//        payments.computeIfPresent(payment.getId(), (k, v) -> {
//            v.setAmount(payment.getAmount());
//            v.setLastUpdateTime(payment.getLastUpdateTime());
//            v.setStatus(payment.getStatus());
//            return v;
//        });
//    }
//}