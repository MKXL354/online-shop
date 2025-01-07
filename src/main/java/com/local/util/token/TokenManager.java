package com.local.util.token;

import java.util.Map;

public interface TokenManager {
    String getToken(Map<String, Object> claims);
    Map<String, Object> getClaims(String token) throws InvalidTokenException, TokenExpiredException;
}
