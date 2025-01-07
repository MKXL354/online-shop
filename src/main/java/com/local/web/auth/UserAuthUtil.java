package com.local.web.auth;

import com.local.model.UserType;
import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import com.local.util.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserAuthUtil {
    private TokenManager tokenManager;
    private UserAuthHandlerRegistry userAuthHandlerRegistry;

    @Autowired
    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Autowired
    public void setUserAuthHandlerRegistry(UserAuthHandlerRegistry userAuthHandlerRegistry) {
        this.userAuthHandlerRegistry = userAuthHandlerRegistry;
    }

    public Map<String, Object> validateToken(String token, AuthRequired authRequired) throws InvalidTokenException, TokenExpiredException {
        Map<String, Object> tokenClaims = tokenManager.getClaims(token);
        for (UserType userType : authRequired.value()) {
            if(userAuthHandlerRegistry.getUserAuthHandler(userType).validateClaims(tokenClaims)){
                return tokenManager.getClaims(token);
            }
        }
        throw new InvalidTokenException("invalid claims", null);
    }
}
