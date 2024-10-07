package com.local.service;

import com.local.util.PropertyManager;

import java.util.Map;

public abstract class TokenManager {
    protected PropertyManager propertyManager;

    public TokenManager(String configFileLocation) {
        propertyManager = new PropertyManager(configFileLocation);
    }

    public abstract String getJwsToken(Map<String, Object> claims);
}
