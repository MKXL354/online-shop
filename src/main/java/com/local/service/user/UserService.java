package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.product.ProductDAO;
import com.local.dao.user.UserDAO;
import com.local.model.*;
import com.local.service.TransactionException;
import com.local.service.productmanagement.InvalidProductCountException;
import com.local.service.productmanagement.ProductNotFoundException;
import com.local.util.lock.LockManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public class UserService {
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;
    private LockManager lockManager;

    public UserService(CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO, PaymentDAO paymentDAO, LockManager lockManager) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
        this.lockManager = lockManager;
    }

    public Cart getCart(User user) throws DAOException{
        Cart cart;
        if((cart = cartDAO.getActiveCart(user)) == null){
            return cartDAO.addCartToUser(user);
        }
        return cart;
    }

    public void addProductToCart(User user, Product product) throws InvalidProductCountException, ProductNotFoundException, DAOException {
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
                    throw new InvalidProductCountException("product count can't be non positive", null);
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
            updatedProducts.add(mainProduct);
        }
        return updatedProducts;
    }

    public int finalizePurchase(User user) throws PreviousPaymentPendingException, EmptyCartException, InsufficientProductCountException, TransactionException, DAOException {
        Cart cart = getCart(user);
        ReentrantLock cartLock = lockManager.getLock(Cart.class, cart.getId());
        cartLock.lock();

        LinkedList<ReentrantLock> productLocks = new LinkedList<>();
        Set<Product> cartProducts = cartDAO.getProductsInCart(cart.getId());
        for(Product product : cartProducts){
            ReentrantLock productLock = lockManager.getLock(Product.class, product.getId());
            productLock.lock();
            productLocks.add(productLock);
        }

        try{
            if(paymentDAO.getActivePayment(user) != null){
                throw new PreviousPaymentPendingException("a previous payment is pending", null);
            }
            if(cartProducts.isEmpty()){
                throw new EmptyCartException("user cart is empty", null);
            }

            double totalPrice = 0;
            for(Product product : cartProducts){
                totalPrice += product.getPrice() * product.getCount();
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
//            TODO: note the catastrophic transaction exception that can leave the system in incorrect state
                throw new TransactionException("catastrophic transaction exception occurred", null);
            }

            return payment.getId();
        }
        finally{
            cartLock.unlock();
            for(ReentrantLock productLock : productLocks){
                productLock.unlock();
            }
        }
    }

//    TODO: send paymentId in web request
    public void rollbackPurchase(Payment payment) throws PaymentNotPendingException, InsufficientProductCountException, TransactionException, DAOException {
        if(payment.getStatus() != PaymentStatus.PENDING){
            throw new PaymentNotPendingException("payment is not pending", null);
        }
        Cart activeCart = cartDAO.getActiveCart(payment.getUser());
        ReentrantLock activeCartLock = lockManager.getLock(Cart.class, activeCart.getId());
        ReentrantLock paymentLock = lockManager.getLock(Payment.class, payment.getId());
        activeCartLock.lock();
        paymentLock.lock();

        LinkedList<ReentrantLock> oldProductsLocks = new LinkedList<>();
        Set<Product> oldCartProducts = payment.getCart().getProducts();
        for(Product product : oldCartProducts){
            ReentrantLock productLock = lockManager.getLock(Product.class, product.getId());
            productLock.lock();
            oldProductsLocks.add(productLock);
        }

        try{
            Set<Product> updatedProducts = getUpdatedProducts(oldCartProducts, true);
            payment.setStatus(PaymentStatus.FAILED);
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
//            TODO: note the catastrophic transaction exception that can leave the system in incorrect state
                throw new TransactionException("catastrophic transaction exception occurred", null);
            }
        }
        finally {
            activeCartLock.unlock();
            paymentLock.unlock();
            for(ReentrantLock productLock: oldProductsLocks){
                productLock.unlock();
            }
        }
    }
}
