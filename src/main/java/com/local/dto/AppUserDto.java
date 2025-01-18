package com.local.dto;

import com.local.entity.AppUser;
import com.local.entity.UserType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AppUserDto extends AbstractDto {
    @Size(max = 255)
    @NotEmpty
    private String username;

    @Size(max = 64)
    @NotEmpty
    private String password;

    @NotNull
    private UserType userType;

    public AppUserDto(AppUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.password = appUser.getPassword();
        this.userType = appUser.getUserType();
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
