package com.local.web.auth;

import com.local.model.UserType;

import java.util.Map;

public abstract class UserAuthHandler {
    protected Map<String, Object> userClaims;

    public abstract UserType getUserType();

    public boolean validateClaims(Map<String, Object> claims) {
        for(Map.Entry<String, Object> entry : userClaims.entrySet()){
            if(!entry.getValue().equals(claims.get(entry.getKey()))){
                return false;
            }
        }
        return true;
    }
}
