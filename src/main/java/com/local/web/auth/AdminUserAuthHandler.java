package com.local.web.auth;

import com.local.entity.UserType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class AdminUserAuthHandler extends UserAuthHandler {
    public AdminUserAuthHandler() {
        userClaims = new HashMap<>();
        userClaims.put("role", UserType.ADMIN.toString());
    }

    @Override
    public UserType getUserType() {
        return UserType.ADMIN;
    }
}
