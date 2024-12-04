package com.local.dao.factory;

import com.local.dao.CartDAO;
import com.local.dao.PaymentDAO;
import com.local.dao.ProductDAO;
import com.local.dao.UserDAO;

public interface DAOFactory {
    CartDAO getCartDAO();
    ProductDAO getProductDAO();
    PaymentDAO getPaymentDAO();
    UserDAO getUserDAO();
}
