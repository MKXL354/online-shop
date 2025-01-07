package com.local.service;

import com.local.persistence.DAOException;
import com.local.persistence.UserDAO;
import com.local.persistence.transaction.ManagedTransaction;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.util.password.PasswordEncryptor;
import com.local.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserManagementService {
    private UserDAO userDAO;
    private PasswordEncryptor passwordEncryptor;

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Autowired
    public void setPasswordEncryptor(PasswordEncryptor passwordEncryptor) {
        this.passwordEncryptor = passwordEncryptor;
    }

    @ManagedTransaction
    public User addUser(User user) throws DuplicateUsernameException, DAOException {
        if(userDAO.getUserByUsername(user.getUsername()) != null) {
            throw new DuplicateUsernameException("duplicate username not allowed", null);
        }
        String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userDAO.addUser(user);
    }

    public User login(String username, String password) throws UserNotFoundException, WrongPasswordException, DAOException {
        User user;
        if((user = userDAO.getUserByUsername(username)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        if(!passwordEncryptor.checkPassword(password, user.getPassword())) {
            throw new WrongPasswordException("wrong password", null);
        }
        return user;
    }
}
