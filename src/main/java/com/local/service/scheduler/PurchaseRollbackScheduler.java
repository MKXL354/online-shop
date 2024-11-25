package com.local.service.scheduler;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.product.ProductDAO;
import com.local.model.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.Executors;

public class PurchaseRollbackScheduler extends RollbackScheduler{
    private PaymentDAO paymentDAO;
    private ProductDAO productDAO;

    public PurchaseRollbackScheduler(int waitBetweenRollbacksMillis, int waitBeforeRollbackMillis, PaymentDAO paymentDAO, ProductDAO productDAO) {
        super(waitBetweenRollbacksMillis, waitBeforeRollbackMillis);
        this.paymentDAO = paymentDAO;
        this.productDAO = productDAO;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
    }

//    TODO: TM here? no reading but updating of products? have update lock too -> List of locks in TM
    protected void rollback() {
        try{
            HashSet<Payment> pendingPayments = paymentDAO.getAllPendingPayments();
            Cart cart;
            for (Payment payment : pendingPayments) {
                cart = payment.getCart();
                if(Duration.between(cart.getProcessTime(), LocalDateTime.now()).toMillis() > waitBeforeRollbackMillis){
                    for(Product product : cart.getProducts()){
                        product.setStatus(ProductStatus.AVAILABLE);
                        productDAO.updateProduct(product);
                    }
                    payment.setStatus(PaymentStatus.FAILED);
                    payment.setLastUpdate(LocalDateTime.now());
                    paymentDAO.updatePayment(payment);
                }
            }
        }
        catch(DAOException e){
//            TODO: better exception handling. maybe log?
            e.printStackTrace();
        }
    }
}
