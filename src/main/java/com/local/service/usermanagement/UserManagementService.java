package com.local.service.usermanagement;

import com.local.dao.DAOException;
import com.local.dao.UserDAO;
import com.local.dao.transaction.ManagedTransaction;
import com.local.exception.service.usermanagement.DuplicateUsernameException;
import com.local.exception.service.usermanagement.UserNotFoundException;
import com.local.exception.service.usermanagement.WrongPasswordException;
import com.local.util.password.PasswordEncryptor;
import com.local.model.User;

public interface UserManagementService {
    @ManagedTransaction
    User addUser(User user) throws DuplicateUsernameException, DAOException;
    User login(String username, String password) throws UserNotFoundException, WrongPasswordException, DAOException;
}
