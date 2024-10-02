package com.local.model;

import com.local.service.UserType;
import com.local.validator.NotNull;

public class User {
    @NotNull(message = "id can't be null")
    private final int id;

    @NotNull(message = "username can't be null")
    private String username;

    @NotNull(message = "password can't be null")
    private String password;

    @NotNull(message = "userType can't be null")
    private UserType userType;

    public User(int id, String username, String password, UserType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
