package com.local.service.user;

import com.local.dao.DAOException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.*;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.util.transaction.ManagedTransaction;

public interface UserService {
    Cart getActiveCart(int userId) throws UserNotFoundException, DAOException;

    @ManagedTransaction
    void addProductToCart(int userId, String productName) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException, DAOException;
    void removeProductFromCart(int userId, String productName) throws UserNotFoundException, ProductNotFoundException, DAOException;
    void finalizePurchase(int userId) throws UserNotFoundException, EmptyCartException, DAOException;
}

//TODO: cache pending payments and drop extra requests
