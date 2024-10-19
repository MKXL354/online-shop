package com.local.model;

import com.local.util.objectvalidator.NotNull;

public class User {
    private int id;

    @NotNull(message = "username cant be null")
    private String username;

    @NotNull(message = "password cant be null")
    private String password;

    @NotNull(message = "wrong or null user type")
    private UserType type;

    public User(int id, String username, String password, UserType type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
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

    public UserType getType() {
        return type;
    }
}
//TODO: user dto and json_parser/object_validation for better validation instead of int id as req param?
