package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.product.ProductDAO;
import com.local.dao.user.UserDAO;
import com.local.exception.common.ApplicationRuntimeException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.InsufficientProductCountException;
import com.local.exception.service.user.PaymentNotPendingException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.*;
import com.local.exception.service.TransactionException;
import com.local.exception.service.productmanagement.InvalidProductCountException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.service.UtilityService;
import com.local.util.lock.LockManager;
import com.local.util.logging.ActivityLog;
import com.local.util.logging.BatchLogManager;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {
    private UtilityService utilityService;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private LockManager lockManager;
    private BatchLogManager batchLogManager;
    private int waitBetweenRollbacksMillis;
    private int waitBeforeRollbackMillis;
    private HashSet<Payment> pendingPayments;
    private ScheduledExecutorService scheduler;

    public UserService(UtilityService utilityService, CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO, PaymentDAO paymentDAO, LockManager lockManager, BatchLogManager batchLogManager, int waitBetweenRollbacksMillis, int waitBeforeRollbackMillis) {
        this.utilityService = utilityService;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
        this.lockManager = lockManager;
        this.batchLogManager = batchLogManager;
        this.waitBetweenRollbacksMillis = waitBetweenRollbacksMillis;
        this.waitBeforeRollbackMillis = waitBeforeRollbackMillis;
        this.pendingPayments = new HashSet<>();
    }

    public void startRollbackScheduler(){
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.scheduler.scheduleWithFixedDelay(this::automaticPaymentRollBack, 0, waitBetweenRollbacksMillis, TimeUnit.MILLISECONDS);
    }

    public Cart getCart(User user) throws DAOException{
        Cart cart;
        if((cart = cartDAO.getActiveCart(user)) == null){
            return cartDAO.addCartToUser(user);
        }
        return cart;
    }

    public void addProductToCart(int userId, Product product) throws UserNotFoundException, ProductNotFoundException, DAOException {
        User user = utilityService.getUserById(userId);
        Cart cart = getCart(user);
        if(productDAO.getProductById(product.getId()) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        ReentrantLock lock = lockManager.getLock(Cart.class, cart.getId());
        lock.lock();
        try{
            Product productInCart = cartDAO.getProductInCartById(cart.getId(), product.getId());
            if(productInCart == null){
                if(product.getCount() <= 0){
                    throw new InvalidProductCountException("product count can't be non positive", null);
                }
                cartDAO.addProductToCart(cart, product);
            }
            else{
                if(product.getCount() < 0){
                    throw new InvalidProductCountException("product count can't be negative", null);
                }
                if(product.getCount() == 0){
                    cartDAO.removeProductFromCart(cart, product);
                }
                cartDAO.updateProductInCart(cart, product);
            }
        }
        finally {
            lock.unlock();
        }
    }

    private Set<Product> getUpdatedProducts(Set<Product> cartProducts, boolean isRollback) throws InsufficientProductCountException, DAOException{
        int sign = isRollback ? +1 : -1;
        Set<Product> updatedProducts = new HashSet<>();
        Product mainProduct;
        for(Product orderedProduct : cartProducts){
            mainProduct = productDAO.getProductById(orderedProduct.getId());
            if(!isRollback && mainProduct.getCount() < orderedProduct.getCount()){
                throw new InsufficientProductCountException(orderedProduct.getName() + " ordered count is higher than available count", null);
            }
            mainProduct.setCount(mainProduct.getCount() + sign * orderedProduct.getCount());
            mainProduct.setSold(mainProduct.getSold() - sign * orderedProduct.getCount());
            updatedProducts.add(mainProduct);
        }
        return updatedProducts;
    }

    public void finalizePurchase(int userId) throws UserNotFoundException, PreviousPaymentPendingException, EmptyCartException, InsufficientProductCountException, TransactionException, DAOException {
        User user = utilityService.getUserById(userId);
        Cart cart = getCart(user);
        ReentrantLock cartLock = lockManager.getLock(Cart.class, cart.getId());
        cartLock.lock();

        LinkedList<ReentrantLock> productLocks = new LinkedList<>();
        TreeSet<Product> cartProducts = new TreeSet<>(cartDAO.getProductsInCart(cart.getId()));
        for(Product product : cartProducts){
            ReentrantLock productLock = lockManager.getLock(Product.class, product.getName());
            productLock.lock();
            productLocks.add(productLock);
        }

        try{
            if(paymentDAO.getPendingPayment(user) != null){
                throw new PreviousPaymentPendingException("a previous payment is pending", null);
            }
            if(cartProducts.isEmpty()){
                throw new EmptyCartException("user cart is empty", null);
            }

            BigDecimal totalPrice = new BigDecimal(0);
            for(Product product : cartProducts){
                totalPrice = totalPrice.add(product.getPrice().multiply(new BigDecimal(product.getCount())));
            }
            Payment payment = new Payment(0, user, cart, totalPrice, LocalDateTime.now(), PaymentStatus.PENDING);
            Set<Product> updatedProducts = getUpdatedProducts(cartProducts, false);
            cart.setProcessTime(LocalDateTime.now());

            try{
                cartDAO.updateCart(cart);
                paymentDAO.addPayment(payment);
                for(Product product: updatedProducts){
                    productDAO.updateProduct(product);
                }
            }
            catch(DAOException e){
                throw new TransactionException("catastrophic transaction exception occurred", null);
            }
        }
        finally{
            cartLock.unlock();
            for(ReentrantLock productLock : productLocks){
                productLock.unlock();
            }
        }
    }

    public void rollbackPurchase(int userId) throws UserNotFoundException, PaymentNotPendingException, TransactionException, DAOException {
        Payment payment = utilityService.getPendingPayment(userId);
        if(payment.getStatus() != PaymentStatus.PENDING){
            throw new PaymentNotPendingException("payment is not pending", null);
        }
        Cart activeCart = cartDAO.getActiveCart(payment.getUser());
        ReentrantLock activeCartLock = lockManager.getLock(Cart.class, activeCart.getId());
        ReentrantLock paymentLock = lockManager.getLock(Payment.class, payment.getId());
        activeCartLock.lock();
        paymentLock.lock();

        LinkedList<ReentrantLock> oldProductsLocks = new LinkedList<>();
        TreeSet<Product> oldCartProducts = new TreeSet<>(cartDAO.getProductsInCart(payment.getCart().getId()));
        for(Product product : oldCartProducts){
            ReentrantLock productLock = lockManager.getLock(Product.class, product.getName());
            productLock.lock();
            oldProductsLocks.add(productLock);
        }

        try{
            Set<Product> updatedProducts = getUpdatedProducts(oldCartProducts, true);
            payment.setStatus(PaymentStatus.FAILED);
            payment.setLastUpdate(LocalDateTime.now());
            for(Product product : oldCartProducts){
                cartDAO.addProductToCart(activeCart, product);
            }
            try{
                cartDAO.updateCart(activeCart);
                paymentDAO.updatePayment(payment);
                for(Product product: updatedProducts){
                    productDAO.updateProduct(product);
                }
            }
            catch(DAOException e){
                throw new TransactionException("catastrophic transaction exception occurred", null);
            }
        }
        catch (InsufficientProductCountException e) {}
        finally {
            activeCartLock.unlock();
            paymentLock.unlock();
            for(ReentrantLock productLock: oldProductsLocks){
                productLock.unlock();
            }
        }
    }

    private void automaticPaymentRollBack(){
        HashSet<Payment> pendingPayments;
        try {
            pendingPayments = paymentDAO.getAllPendingPayments();
        } catch (DAOException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
        for(Payment payment : pendingPayments){
            if(Duration.between(payment.getLastUpdate(), LocalDateTime.now()).toMillis() > waitBeforeRollbackMillis){
                try {
                    rollbackPurchase(payment.getUser().getId());
                }
                catch (UserNotFoundException | PaymentNotPendingException e) {
                    throw new ApplicationRuntimeException(e.getMessage(), e);
                }
                catch (TransactionException | DAOException e) {
                    ActivityLog log = new ActivityLog("local", "local");
                    log.createExceptionLog(e);
                    batchLogManager.addLog(log);
                }
            }
        }
    }

    public void shutdownRollbackScheduler(){
        scheduler.shutdownNow();
        automaticPaymentRollBack();
    }
}
//TODO: cache pending payments and drop extra requests
