package com.local.util.token;

import com.local.util.PropertyManager;

import java.util.Map;

public abstract class TokenManager {
    protected PropertyManager propertyManager;

    public TokenManager(String configFileLocation) {
        propertyManager = new PropertyManager(configFileLocation);
    }

    public abstract String getSignedToken(Map<String, Object> claims);

    public abstract Map<String, Object> validateSignedToken(String jws, Map<String, Object> claims) throws InvalidTokenException, TokenExpiredException;
}
