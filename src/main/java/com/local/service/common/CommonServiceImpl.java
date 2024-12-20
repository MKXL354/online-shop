package com.local.service.common;

import com.local.dao.CartDAO;
import com.local.dao.DAOException;
import com.local.dao.PaymentDAO;
import com.local.dao.UserDAO;
import com.local.exception.common.ApplicationRuntimeException;
import com.local.exception.service.common.CartNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Cart;
import com.local.model.Payment;
import com.local.model.User;

public class CommonServiceImpl implements CommonService {
    private UserDAO userDAO;
    private CartDAO cartDAO;
    private PaymentDAO paymentDAO;

    public CommonServiceImpl(UserDAO userDAO, CartDAO cartDAO, PaymentDAO paymentDAO) {
        this.userDAO = userDAO;
        this.cartDAO = cartDAO;
        this.paymentDAO = paymentDAO;
    }

    @Override
    public User getUserById(int userId) throws UserNotFoundException, DAOException {
        User user;
        if((user = userDAO.getUserById(userId)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        return user;
    }

    @Override
    public Cart getActiveCart(int userId) throws UserNotFoundException, DAOException {
        Cart cart = cartDAO.getActiveCart(userId);
        User user = getUserById(userId);
        if(cart == null) {
            cart = cartDAO.addCartToUser(userId);
            cart.setUser(user);
            return cart;
        }
        cart.setUser(user);
        cart.setProducts(cartDAO.getProductsInCart(cart.getId()));
        return cart;
    }

    @Override
    public Cart getCartById(int cartId) throws CartNotFoundException, DAOException {
        Cart cart = cartDAO.getCartById(cartId);
        if(cart == null) {
            throw new CartNotFoundException("cart not found", null);
        }
        try {
            cart.setUser(getUserById(cart.getUser().getId()));
        } catch (UserNotFoundException e) {
            throw new ApplicationRuntimeException("user associated with cart not found", null);
        }
        cart.setProducts(cartDAO.getProductsInCart(cartId));
        return cart;
    }

    @Override
    public Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException {
        Payment payment = paymentDAO.getPendingPayment(userId);
        if(payment == null) {
            return null;
        }
        payment.setUser(getUserById(userId));
        try {
            payment.setCart(getCartById(payment.getCart().getId()));
        } catch (CartNotFoundException e) {
            throw new ApplicationRuntimeException("cart associated with payment not found", null);
        }
        return payment;
    }
}

