package com.local.model;

import com.local.validator.NotNull;

public class User {
    @NotNull(message = "id can't be null")
    private int id;

    @NotNull(message = "username can't be null")
    private String username;

    @NotNull(message = "password can't be null")
    private String password;

    @NotNull(message = "type can't be null")
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
