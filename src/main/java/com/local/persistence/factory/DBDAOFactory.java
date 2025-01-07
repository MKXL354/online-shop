package com.local.persistence.factory;

import com.local.persistence.CartDAO;
import com.local.persistence.PaymentDAO;
import com.local.persistence.ProductDAO;
import com.local.persistence.UserDAO;
import com.local.persistence.db.CartDAODBImpl;
import com.local.persistence.db.PaymentDAODBImpl;
import com.local.persistence.db.ProductDAODBImpl;
import com.local.persistence.db.UserDAODBImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBDAOFactory implements DAOFactory {
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private PaymentDAO paymentDAO;
    private UserDAO userDAO;

    public DBDAOFactory() {
        this.cartDAO = new CartDAODBImpl();
        this.productDAO = new ProductDAODBImpl();
        this.paymentDAO = new PaymentDAODBImpl();
        this.userDAO = new UserDAODBImpl();
    }

    @Override
    public CartDAO getCartDAO() {
        return cartDAO;
    }

    @Override
    public ProductDAO getProductDAO() {
        return productDAO;
    }

    @Override
    public PaymentDAO getPaymentDAO() {
        return paymentDAO;
    }

    @Override
    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Autowired
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Autowired
    public void setPaymentDAO(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setCartDAO(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }
}
