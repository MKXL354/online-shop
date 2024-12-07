package com.local.service;

import com.local.dao.DAOException;
import com.local.dao.UserDAO;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.model.User;

public class CommonService {
    private UserDAO userDAO;

    public CommonService(UserDAO userDAO) {
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
