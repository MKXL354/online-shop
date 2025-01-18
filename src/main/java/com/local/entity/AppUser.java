package com.local.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class AppUser extends AbstractEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, length = 511)
    private String password;

    @Column(nullable = false)
    private UserType userType;

    public AppUser() {}

    public AppUser(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
