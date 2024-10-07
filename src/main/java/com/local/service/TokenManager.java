package com.local.service;

import com.local.util.PropertyManager;

import java.util.Map;

public abstract class TokenManager {
    protected PropertyManager propertyManager;

    public TokenManager(String configFileLocation) {
        propertyManager = new PropertyManager(configFileLocation);
    }

    public abstract String getSignedToken(Map<String, Object> claims);
//    TODO: maybe add normal and encrypted token as well
}
