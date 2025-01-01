package com.local.service;

import com.local.dao.DAOException;
import com.local.dao.CartDAO;
import com.local.dao.PaymentDAO;
import com.local.dao.ProductDAO;
import com.local.dao.transaction.ManagedTransaction;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.*;
import com.local.scheduler.TaskScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserService {
//    TODO: using Spring Transaction Manager with this is
    private UserService proxy;
    private CommonService commonService;
    private TaskScheduler taskScheduler;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private PaymentDAO paymentDAO;

    @Autowired
    public void setProxy(UserService proxy) {
        this.proxy = proxy;
    }

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }

    @Autowired
    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Autowired
    public void setCartDAO(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

   @ManagedTransaction
    public void addProductToCart(int userId, String productName) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException, DAOException {
        if(commonService.getPendingPayment(userId) != null){
            throw new PreviousPaymentPendingException("a previous payment is pending", null);
        }
        Cart cart = commonService.getActiveCart(userId);
        Product product;
        if((product = productDAO.getProductByName(productName)) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        product.setProductStatus(ProductStatus.RESERVED);
        productDAO.updateProduct(product);
        cartDAO.addProductToCart(cart, product);

        cart.setLastUpdateTime(LocalDateTime.now());
        cartDAO.updateCart(cart);
        taskScheduler.submitTask(() -> {
            proxy.removeProductFromCart(userId, product.getId());
            return null;
        }, 10 * 60 * 1000);
    }

   @ManagedTransaction
    public void removeProductFromCart(int userId, int productId) throws UserNotFoundException, ProductNotFoundException, DAOException{
        Cart cart = commonService.getActiveCart(userId);
        Product product = cart.getProducts().get(productId);
        if(product == null){
            throw new ProductNotFoundException("product not found in cart", null);
        }
        product.setProductStatus(ProductStatus.AVAILABLE);
        productDAO.updateProduct(product);
        cartDAO.removeProductFromCart(cart, product);
        cart.setLastUpdateTime(LocalDateTime.now());
        cartDAO.updateCart(cart);
    }

   @ManagedTransaction
    public void finalizePurchase(int userId) throws UserNotFoundException, EmptyCartException, DAOException {
        User user = commonService.getUserById(userId);
        Cart cart = commonService.getActiveCart(userId);
        Map<Integer, Product> cartProducts = cart.getProducts();
        if(cartProducts.isEmpty()){
            throw new EmptyCartException("user cart is empty", null);
        }

        BigDecimal totalPrice = new BigDecimal(0);
        for(Product product : cartProducts.values()){
            totalPrice = totalPrice.add(product.getPrice());
            product.setProductStatus(ProductStatus.SOLD);
            productDAO.updateProduct(product);
        }
        LocalDateTime now = LocalDateTime.now();
        Payment payment = new Payment(0, user, cart, totalPrice, now, PaymentStatus.PENDING);
        paymentDAO.addPayment(payment);

        cart.setLastUpdateTime(now);
        cart.setCartStatus(CartStatus.PROCESSED);
        cartDAO.updateCart(cart);
        taskScheduler.submitTask(() -> {
            proxy.cancelPurchase(userId);
            return null;
        }, 5 * 60 * 1000);
    }

   @ManagedTransaction
    public void cancelPurchase(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, DAOException {
        Payment payment = commonService.getPendingPayment(userId);
        if(payment == null){
            throw new PendingPaymentNotFoundException("no active payment found", null);
        }
        Cart cart = payment.getCart();
        for(Product product : cart.getProducts().values()){
            product.setProductStatus(ProductStatus.AVAILABLE);
            productDAO.updateProduct(product);
        }
        LocalDateTime now = LocalDateTime.now();

        cart.setCartStatus(CartStatus.CANCELLED);
        cart.setLastUpdateTime(now);
        cartDAO.updateCart(cart);

        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setLastUpdateTime(now);
        paymentDAO.updatePayment(payment);
    }
}
