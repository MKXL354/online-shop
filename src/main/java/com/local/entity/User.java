package com.local.entity;

import com.local.util.password.PasswordConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;

@Entity
public class User extends AbstractEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false, length = 511)
    @Convert(converter = PasswordConverter.class)
    private String password;

    @Column(nullable = false)
    private UserType userType;

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
