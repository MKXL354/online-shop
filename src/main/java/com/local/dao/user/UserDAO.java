package com.local.dao.user;

import com.local.model.User;

public interface UserDAO {
    void addUser(User user) throws UserDAOException;
    void updateUser(User user) throws UserDAOException;
    void deleteUser(int id) throws UserDAOException;
    User findUserById(int id) throws UserDAOException;
    User findUserByUsername(String username) throws UserDAOException;
//    TODO: list of all users
}
