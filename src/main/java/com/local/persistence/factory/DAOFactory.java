package com.local.persistence.factory;

import com.local.persistence.CartDAO;
import com.local.persistence.PaymentDAO;
import com.local.persistence.ProductDAO;
import com.local.persistence.UserDAO;

public interface DAOFactory {
    CartDAO getCartDAO();
    ProductDAO getProductDAO();
    PaymentDAO getPaymentDAO();
    UserDAO getUserDAO();
}
