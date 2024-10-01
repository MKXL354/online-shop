package com.local.service;

import com.local.commonexceptions.ServiceException;
import com.local.dao.UserDAO;
import com.local.model.User;
import com.local.model.WebUser;

public class UserManagementService {
    private UserDAO userDAO;

    public UserManagementService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void addUser(String username, String password) throws ServiceException {
        User user = new WebUser(0, username, password);
        userDAO.insertUser(user);
    }
}
