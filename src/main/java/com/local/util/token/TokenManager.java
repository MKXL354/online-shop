package com.local.util.token;

import java.util.Map;

public interface TokenManager {
    String getSignedToken(Map<String, Object> claims);
    Map<String, Object> getSignedClaims(String jws) throws InvalidTokenException, TokenExpiredException;
}
