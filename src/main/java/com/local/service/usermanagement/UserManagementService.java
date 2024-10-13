package com.local.service.usermanagement;

import com.local.dao.user.UserDAO;
import com.local.dao.user.UserDAOException;
import com.local.util.password.PasswordEncryptor;
import com.local.model.User;

public class UserManagementService {
    private UserDAO userDAO;
    private PasswordEncryptor passwordEncryptor;

    public UserManagementService(UserDAO userDAO, PasswordEncryptor passwordEncryptor) {
        this.userDAO = userDAO;
        this.passwordEncryptor = passwordEncryptor;
    }

    public void addUser(User user) throws DuplicateUsernameException, UserDAOException {
        if(userDAO.findUserByUsername(user.getUsername()) != null) {
            throw new DuplicateUsernameException("duplicate username not allowed", null);
        }
        String hashedPassword = passwordEncryptor.hashPassword(user.getPassword());
        User DBUser = new User(user.getId(), user.getUsername(), hashedPassword, user.getType());
        userDAO.addUser(DBUser);
    }

    public User login(String username, String password) throws UserNotFoundException, WrongPasswordException, UserDAOException {
        User user = userDAO.findUserByUsername(username);
        if(user == null) {
            throw new UserNotFoundException("user not found", null);
        }
        if(!passwordEncryptor.checkPassword(password, user.getPassword())) {
            throw new WrongPasswordException("wrong password", null);
        }
        return user;
    }
}
