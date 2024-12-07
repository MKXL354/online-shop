package com.local.service.user;

import com.local.dao.DAOException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.dao.transaction.ManagedTransaction;

public interface UserService {
    @ManagedTransaction
    void addProductToCart(int userId, String productName) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException, DAOException;
    @ManagedTransaction
    void removeProductFromCart(int userId, String productName) throws UserNotFoundException, ProductNotFoundException, DAOException;
    @ManagedTransaction
    void finalizePurchase(int userId) throws UserNotFoundException, EmptyCartException, DAOException;
}

//TODO: cache pending payments and drop extra requests
