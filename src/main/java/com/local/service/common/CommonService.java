package com.local.service.common;

import com.local.dao.DAOException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Cart;
import com.local.model.Payment;
import com.local.model.User;

public interface CommonService {
    User getUserById(int id) throws UserNotFoundException, DAOException;
    Cart getActiveCart(int userId) throws UserNotFoundException, DAOException;
    Cart getCartById(int cartId) throws UserNotFoundException, DAOException;
    Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException;
}
