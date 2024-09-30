package com.local.service;

import com.local.dao.UserDAO;
import com.local.model.User;
import com.local.model.WebUser;

import java.util.UUID;

public class UserManagementService {
    private UserDAO userDAO;

    public UserManagementService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void addUser(String username, String password) {
//        TODO: a way of indicating success/failure: exception, boolean, null, string message?
        UUID id = UUID.randomUUID();
//        TODO: add cart and logic. Determine for old carts to be saved or not then decide on this
        User user = new WebUser(id, username, password, null);
        userDAO.insertUser(user);
        System.out.println("success");
    }
}
