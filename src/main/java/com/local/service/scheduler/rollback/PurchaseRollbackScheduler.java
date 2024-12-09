//package com.local.service.scheduler.rollback;
//
//import com.local.dao.PaymentDAO;
//import com.local.dao.ProductDAO;
//
//import java.util.concurrent.Executors;
//
//public class PurchaseRollbackScheduler extends TaskScheduler{
//    private PaymentDAO paymentDAO;
//    private ProductDAO productDAO;
//
//    public PurchaseRollbackScheduler(int waitBetweenRollbacksMillis, int waitBeforeRollbackMillis, PaymentDAO paymentDAO, ProductDAO productDAO) {
//        super(waitBetweenRollbacksMillis, waitBeforeRollbackMillis);
//        this.paymentDAO = paymentDAO;
//        this.productDAO = productDAO;
//        this.scheduler = Executors.newSingleThreadScheduledExecutor();
//    }
//
//    protected void rollback() {
////        try{
////            HashSet<Payment> rollbackCandidates = paymentDAO.getAllPendingPayments();
////            rollbackCandidates.addAll(paymentDAO.getAllCancelledPayments());
////            Cart cart;
////            for (Payment payment : rollbackCandidates) {
////                cart = payment.getCart();
////                if(Duration.between(cart.getLastUpdateTime(), LocalDateTime.now()).toMillis() > waitBeforeRollbackMillis){
////                    for(Product product : cart.getProducts().values()){
////                        product.setProductStatus(ProductStatus.AVAILABLE);
////                        productDAO.updateProduct(product);
////                    }
////                    payment.setPaymentStatus(PaymentStatus.FAILED);
////                    payment.setLastUpdateTime(LocalDateTime.now());
////                    paymentDAO.updatePayment(payment);
////                }
////            }
////        }
////        catch(DAOException e){
//////            TODO: better exception handling. maybe log?
////            e.printStackTrace();
////        }
//    }
//}
