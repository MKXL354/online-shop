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

    private double balance;

    public User(int id, String username, String password, UserType type, double balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.balance = balance;
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

    public double getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
