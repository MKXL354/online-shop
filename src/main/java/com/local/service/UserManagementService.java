package com.local.service;

import com.local.dao.DataAccessException;
import com.local.dao.PasswordEncryptor;
import com.local.dao.UserDAO;
import com.local.model.User;

public class UserManagementService {
    private UserDAO userDAO;
    private PasswordEncryptor passwordEncryptor;

    public UserManagementService(UserDAO userDAO, PasswordEncryptor passwordEncryptor) {
        this.userDAO = userDAO;
        this.passwordEncryptor = passwordEncryptor;
    }

    public void addUser(User user) throws ServiceException {
        try {
            if(userDAO.findUserByUsername(user.getUsername()) != null) {
                throw new ServiceException("duplicate username not allowed", null);
            }
            String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
            User DBUser = new User(user.getId(), user.getUsername(), hashedPassword, user.getType());
            userDAO.addUser(DBUser);
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

    public User login(String username, String password) throws ServiceException {
        try {
            User user = userDAO.findUserByUsername(username);
            if(user == null) {
                throw new ServiceException("user not found", null);
            }
            if(!passwordEncryptor.checkPassword(password, user.getPassword())) {
                throw new ServiceException("wrong password", null);
            }
            return user;
        } catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }
}
//TODO: rewrite update and delete. especially when checking for service constraints
