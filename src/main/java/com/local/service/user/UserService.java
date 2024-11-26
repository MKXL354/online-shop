package com.local.service.user;

import com.local.dao.DAOException;
import com.local.dao.cart.CartDAO;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.product.ProductDAO;
import com.local.dao.user.UserDAO;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.*;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.service.UtilityService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class UserService {
    private UtilityService utilityService;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;

    public UserService(UtilityService utilityService, CartDAO cartDAO, ProductDAO productDAO, UserDAO userDAO, PaymentDAO paymentDAO) {
        this.utilityService = utilityService;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
    }

//    TODO: get products in cart endpoint?
    private Cart getCart(User user) throws DAOException{
        Cart cart;
        if((cart = cartDAO.getActiveCart(user)) == null){
            return cartDAO.addCartToUser(user);
        }
        return cart;
    }

//    TODO: TM here
//    TODO: reservation rollback time (maybe in a separate scheduler)
    /*
    if there is a pending payment, no product can be added to a new cart
     */
    public void addProductToCart(int userId, String productName) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException, DAOException {
        User user = utilityService.getUserById(userId);
        if(paymentDAO.getPendingPayment(user) != null){
            throw new PreviousPaymentPendingException("a previous payment is pending", null);
        }
        Cart cart = getCart(user);
        Product product;
        if((product = productDAO.getProductByName(productName)) == null){
            throw new ProductNotFoundException("product not found", null);
        }
        product.setStatus(ProductStatus.RESERVED);
        productDAO.updateProduct(product);
        cartDAO.addProductToCart(cart, product);

        cart.setLastUpdateTime(LocalDateTime.now());
        cartDAO.updateCart(cart);
    }

//    TODO: add the endpoint for removeProduct
    public void removeProductFromCart(int userId, String productName) throws UserNotFoundException, ProductNotFoundException, DAOException{
        User user = utilityService.getUserById(userId);
        Cart cart = getCart(user);
        for(Product product : cart.getProducts()){
            if(product.getName().equals(productName)){
                product.setStatus(ProductStatus.AVAILABLE);
                productDAO.updateProduct(product);
                cartDAO.removeProductFromCart(cart, product);
                cart.setLastUpdateTime(LocalDateTime.now());
                cartDAO.updateCart(cart);
                return;
            }
        }
        throw new ProductNotFoundException("product not found in cart", null);
    }

//    TODO: TM here
    /*
    products are considered sold without payment. so in time of rollback the status must change
     */
    public void finalizePurchase(int userId) throws UserNotFoundException, EmptyCartException, DAOException {
        User user = utilityService.getUserById(userId);
        Cart cart = getCart(user);
        Set<Product> cartProducts = cart.getProducts();
        if(cartProducts.isEmpty()){
            throw new EmptyCartException("user cart is empty", null);
        }

        BigDecimal totalPrice = new BigDecimal(0);
        for(Product product : cartProducts){
            totalPrice = totalPrice.add(product.getPrice());
            product.setStatus(ProductStatus.SOLD);
            productDAO.updateProduct(product);
        }
        LocalDateTime now = LocalDateTime.now();
        Payment payment = new Payment(0, user, cart, totalPrice, now, PaymentStatus.PENDING);
        paymentDAO.addPayment(payment);

        cart.setProcessTime(now);
        cart.setLastUpdateTime(now);
        cartDAO.updateCart(cart);
    }
}
//TODO: cache pending payments and drop extra requests
//TODO: add order cancelling and remove product from cart
