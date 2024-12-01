package com.local.service;

import com.local.dao.DAOException;
import com.local.dao.user.UserDAO;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.User;

public class UtilityService {
    private UserDAO userDAO;

    public UtilityService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User getUserById(int id) throws UserNotFoundException, DAOException {
        User user;
        if((user = userDAO.getUserById(id)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        return user;
    }
}
