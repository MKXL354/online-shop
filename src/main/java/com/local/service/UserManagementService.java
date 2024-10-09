package com.local.service;

import com.local.dao.UserDAO;
import com.local.dao.UserDAOException;
import com.local.dao.PasswordEncryptor;
import com.local.model.User;

public class UserManagementService {
    private UserDAO userDAO;
    private PasswordEncryptor passwordEncryptor;

    public UserManagementService(UserDAO userDAO, PasswordEncryptor passwordEncryptor) {
        this.userDAO = userDAO;
        this.passwordEncryptor = passwordEncryptor;
    }

    public void addUser(User user) throws UserManagementServiceException {
        try {
            if(userDAO.findUserByUsername(user.getUsername()) != null) {
                throw new UserManagementServiceException("duplicate username not allowed", null);
            }
            String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
            User DBUser = new User(user.getId(), user.getUsername(), hashedPassword, user.getType());
            userDAO.addUser(DBUser);
        } catch (UserDAOException e) {
            throw new UserManagementServiceException(e.getMessage(), e);
        }
    }

    public void updateUser(User user) throws UserManagementServiceException {
        try {
            userDAO.updateUser(user);
        } catch (UserDAOException e) {
            throw new UserManagementServiceException(e.getMessage(), e);
        }
    }

    public void deleteUser(int id) throws UserManagementServiceException {
        try {
            userDAO.deleteUser(id);
        } catch (UserDAOException e) {
            throw new UserManagementServiceException(e.getMessage(), e);
        }
    }

    public User login(String username, String password) throws UserManagementServiceException {
        try {
            User user = userDAO.findUserByUsername(username);
            if(user == null) {
                throw new UserManagementServiceException("user not found", null);
            }
            if(!passwordEncryptor.checkPassword(password, user.getPassword())) {
                throw new UserManagementServiceException("wrong password", null);
            }
            return user;
        } catch (UserDAOException e) {
            throw new UserManagementServiceException(e.getMessage(), e);
        }
    }
}
