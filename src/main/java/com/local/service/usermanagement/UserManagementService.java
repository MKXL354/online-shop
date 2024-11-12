package com.local.service.usermanagement;

import com.local.dao.DAOException;
import com.local.dao.user.UserDAO;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.util.lock.LockManager;
import com.local.util.password.PasswordEncryptor;
import com.local.model.User;

import java.util.concurrent.locks.ReentrantLock;

public class UserManagementService {
    private UserDAO userDAO;
    private PasswordEncryptor passwordEncryptor;
    private LockManager lockManager;

    public UserManagementService(UserDAO userDAO, PasswordEncryptor passwordEncryptor, LockManager lockManager) {
        this.userDAO = userDAO;
        this.passwordEncryptor = passwordEncryptor;
        this.lockManager = lockManager;
    }

    public User addUser(User user) throws DuplicateUsernameException, DAOException {
        String username = user.getUsername();
        ReentrantLock lock = lockManager.getLock(User.class, username);
        lock.lock();
        try{
            if(userDAO.getUserByUsername(user.getUsername()) != null) {
                throw new DuplicateUsernameException("duplicate username not allowed", null);
            }
            String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            return userDAO.addUser(user);
        }
        finally {
            lock.unlock();
        }
    }

    public User getUserById(int id) throws UserNotFoundException, DAOException {
        User user;
        if((user = userDAO.getUserById(id)) == null) {
            throw new UserNotFoundException("user not found", null);
        }
        return user;
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
