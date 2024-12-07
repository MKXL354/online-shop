package com.local.dao.factory;

import com.local.dao.CartDAO;
import com.local.dao.PaymentDAO;
import com.local.dao.ProductDAO;
import com.local.dao.UserDAO;
import com.local.dao.db.CartDAODBImpl;
import com.local.dao.db.PaymentDAODBImpl;
import com.local.dao.db.ProductDAODBImpl;
import com.local.dao.db.UserDAODBImpl;
import com.local.dao.transaction.TransactionManager;

public class DBDAOFactory implements DAOFactory {
    private TransactionManager transactionManager;
    private CartDAO cartDAO;
    private ProductDAO productDAO;
    private PaymentDAO paymentDAO;
    private UserDAO userDAO;

    public DBDAOFactory(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.cartDAO = new CartDAODBImpl(transactionManager);
        this.productDAO = new ProductDAODBImpl(transactionManager);
        this.paymentDAO = new PaymentDAODBImpl(transactionManager);
        this.userDAO = new UserDAODBImpl(transactionManager);
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
}
