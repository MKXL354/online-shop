package com.local.service;

import com.local.dao.DataAccessException;
import com.local.dao.UserDAO;
import com.local.model.User;

public class UserManagementService {
    private UserDAO userDAO;

    public UserManagementService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void addUser(User user) throws ServiceException {
        try {
            userDAO.insertUser(user);
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void updateUser(User user) throws ServiceException {
        try {
            userDAO.updateUser(user);
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public void deleteUser(int id) throws ServiceException {
        try {
            userDAO.deleteUser(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public User getUserById(int id) throws ServiceException {
        try {
            return userDAO.findUserById(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
