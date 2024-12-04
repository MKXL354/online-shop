package com.local.service.usermanagement;

import com.local.dao.DAOException;
import com.local.dao.UserDAO;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.util.password.PasswordEncryptor;
import com.local.model.User;

public class UserManagementServiceImpl implements UserManagementService{
    private UserDAO userDAO;
    private PasswordEncryptor passwordEncryptor;

    public UserManagementServiceImpl(UserDAO userDAO, PasswordEncryptor passwordEncryptor) {
        this.userDAO = userDAO;
        this.passwordEncryptor = passwordEncryptor;
    }

    @Override
    public User addUser(User user) throws DuplicateUsernameException, DAOException {
        if(userDAO.getUserByUsername(user.getUsername()) != null) {
            throw new DuplicateUsernameException("duplicate username not allowed", null);
        }
        String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);
        return userDAO.addUser(user);
    }

    @Override
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
