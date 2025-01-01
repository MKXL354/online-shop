package com.local.filter.auth;

import com.local.model.UserType;

import java.util.HashMap;

public class WebUserAuthorizationFilter extends UserAuthorizationFilter {
    @Override
    protected HashMap<String, Object> setClaims() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", UserType.WEB_USER.toString());
        return claims;
    }
}
