package com.local.model;

import com.local.util.objectvalidator.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = -6766227676937315014L;

    private int id;

    @NotNull(message = "username can't be null")
    private String username;

    @NotNull(message = "password can't be null")
    private String password;

    @NotNull(message = "wrong or null user type")
    private UserType type;

    @NotNull(message = "user balance can't be null")
    private BigDecimal balance;

    public User(int id, String username, String password, UserType type, BigDecimal balance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.type = type;
        this.balance = balance;
    }

    public User(User user){
        this(user.getId(), user.getUsername(), user.getPassword(), user.getType(), user.getBalance());
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

    public BigDecimal getBalance() {
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

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
