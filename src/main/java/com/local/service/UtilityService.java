package com.local.service;

import com.local.dao.DAOException;
import com.local.dao.payment.PaymentDAO;
import com.local.dao.user.UserDAO;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.Payment;
import com.local.model.User;

public class UtilityService {
    private UserDAO userDAO;
    private PaymentDAO paymentDAO;

    public UtilityService(UserDAO userDAO, PaymentDAO paymentDAO) {
        this.userDAO = userDAO;
        this.paymentDAO = paymentDAO;
    }

    public User getUserById(int id) throws UserNotFoundException, DAOException {
        User user;
        if((user = userDAO.getUserById(id)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        return user;
    }

    public Payment getPendingPayment(int userId) throws UserNotFoundException, DAOException {
        User user = getUserById(userId);
        return paymentDAO.getPendingPayment(user);
    }
}
