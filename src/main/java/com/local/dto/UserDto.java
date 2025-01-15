package com.local.dto;

import com.local.entity.User;
import com.local.entity.UserType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class UserDto extends AbstractDto {
    @Size(max = 255)
    @NotEmpty
    private String username;

    @Size(max = 64)
    @NotEmpty
    private String password;

    @NotEmpty
    private UserType userType;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.userType = user.getUserType();
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
