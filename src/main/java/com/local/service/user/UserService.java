package com.local.service.user;

import com.local.dao.DAOException;
import com.local.exception.service.common.CartNotFoundException;
import com.local.exception.service.payment.PendingPaymentNotFoundException;
import com.local.exception.service.user.EmptyCartException;
import com.local.exception.service.user.PreviousPaymentPendingException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.productmanagement.ProductNotFoundException;
import com.local.dao.transaction.ManagedTransaction;
import com.local.model.Cart;

public interface UserService {
    void setProxy(UserService proxy);
    @ManagedTransaction
    void addProductToCart(int userId, String productName) throws UserNotFoundException, PreviousPaymentPendingException, ProductNotFoundException, DAOException;
    @ManagedTransaction
    void removeProductFromCart(int userId, int productId) throws UserNotFoundException, ProductNotFoundException, DAOException;
    @ManagedTransaction
    void finalizePurchase(int userId) throws UserNotFoundException, EmptyCartException, DAOException;
    @ManagedTransaction
    void cancelPurchase(int userId) throws UserNotFoundException, PendingPaymentNotFoundException, DAOException;
}
