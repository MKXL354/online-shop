package com.local.web.interceptor;

import com.local.model.UserType;
import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import com.local.util.token.TokenManager;
import com.local.web.auth.AuthRequired;
import com.local.web.auth.UserAuthHandlerRegistry;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private TokenManager tokenManager;
    private UserAuthHandlerRegistry userAuthHandlerRegistry;

    @Autowired
    public void setTokenManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Autowired
    public void setUserAuthHandlerRegistry(UserAuthHandlerRegistry UserAuthHandlerRegistry) {
        this.userAuthHandlerRegistry = UserAuthHandlerRegistry;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws InvalidTokenException, TokenExpiredException {
        AuthRequired authRequired;
        if(handler instanceof HandlerMethod controller && (authRequired = controller.getMethodAnnotation(AuthRequired.class)) != null){
            String jws = request.getHeader("Authorization");
            Map<String, Object> tokenClaims = tokenManager.getSignedClaims(jws);
            request.setAttribute("userId", ((Double) tokenClaims.get("userId")).intValue());
            for (UserType userType : authRequired.value()) {
                if(userAuthHandlerRegistry.getUserAuthHandler(userType).validateClaims(tokenClaims)){
                    return true;
                }
            }
            throw new InvalidTokenException("invalid claims", null);
        }
        return true;
    }
}
