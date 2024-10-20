package com.local.dao.user;

import com.local.dao.DAOException;
import com.local.model.User;

public interface UserDAO {
    void addUser(User user) throws DAOException;
    User getUserById(int id) throws DAOException;
    User getUserByUsername(String username) throws DAOException;
}
