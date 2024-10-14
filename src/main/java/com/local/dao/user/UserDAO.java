package com.local.dao.user;

import com.local.dao.DAOException;
import com.local.model.User;

public interface UserDAO {
    void addUser(User user) throws DAOException;
    void updateUser(User user) throws DAOException;
    void deleteUser(int id) throws DAOException;
    User findUserById(int id) throws DAOException;
    User findUserByUsername(String username) throws DAOException;
//    TODO: list of all users
}
