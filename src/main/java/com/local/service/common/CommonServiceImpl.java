package com.local.service.common;

import com.local.dao.CartDAO;
import com.local.dao.DAOException;
import com.local.dao.PaymentDAO;
import com.local.dao.UserDAO;
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
    public User getUserById(int id) throws UserNotFoundException, DAOException {
        User user;
        if((user = userDAO.getUserById(id)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        return user;
    }

    @Override
    public Cart getActiveCart(int userId) throws UserNotFoundException, DAOException {
        Cart cart = cartDAO.getActiveCart(userId);
        if(cart == null) {
            return cartDAO.addCartToUser(userId);
        }
        cart.setUser(getUserById(userId));
        cart.setProducts(cartDAO.getProductsInCart(cart.getId()));
        return cart;
    }

    @Override
    public Cart getCartById(int cartId) throws UserNotFoundException, DAOException {
        Cart cart = cartDAO.getCartById(cartId);
        cart.setUser(getUserById(cart.getUser().getId()));
        cart.setProducts(cartDAO.getProductsInCart(cart.getId()));
        return cart;
    }

    @Override
    public Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException {
        Payment payment = paymentDAO.getPendingPayment(userId);
        payment.setUser(getUserById(userId));
        payment.setCart(getCartById(payment.getCart().getId()));
        return payment;
    }
}

