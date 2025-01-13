package com.local.web.auth;

import com.local.entity.UserType;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class WebUserAuthHandler extends UserAuthHandler {
    public WebUserAuthHandler() {
        userClaims = new HashMap<>();
        userClaims.put("role", UserType.WEB_USER.toString());
    }

    @Override
    public UserType getUserType() {
        return UserType.WEB_USER;
    }
}
