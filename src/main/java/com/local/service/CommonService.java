package com.local.service;

import com.local.entity.Cart;
import com.local.entity.Payment;
import com.local.entity.User;
import com.local.exception.common.ApplicationRuntimeException;
import com.local.exception.service.common.CartNotFoundException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.persistence.CartDAO;
import com.local.persistence.DAOException;
import com.local.persistence.PaymentDAO;
import com.local.persistence.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommonService {
    private UserDAO userDAO;
    private CartDAO cartDAO;
    private PaymentDAO paymentDAO;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setCartDAO(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    @Autowired
    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public User getUserById(int userId) throws UserNotFoundException, DAOException {
        User user;
        if ((user = userDAO.getUserById(userId)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        return user;
    }

    public Cart getActiveCart(int userId) throws UserNotFoundException, DAOException {
        Cart cart = cartDAO.getActiveCart(userId);
        User user = getUserById(userId);
        if (cart == null) {
            cart = cartDAO.addCartToUser(userId);
            cart.setUser(user);
            return cart;
        }
        cart.setUser(user);
        cart.setProducts(cartDAO.getProductsInCart(cart.getId()));
        return cart;
    }

    public Cart getCartById(int cartId) throws CartNotFoundException, DAOException {
        Cart cart = cartDAO.getCartById(cartId);
        if (cart == null) {
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

    public Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException {
        Payment payment = paymentDAO.getPendingPayment(userId);
        if (payment == null) {
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
