package com.local.persistence;

import com.local.entity.User;

public interface UserDAO {
    User addUser(User user) throws DAOException;

    void updateUser(User user) throws DAOException;

    User getUserById(int id) throws DAOException;

    User getUserByUsername(String username) throws DAOException;
}
