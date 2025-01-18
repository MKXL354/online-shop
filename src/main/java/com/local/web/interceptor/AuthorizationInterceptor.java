package com.local.web.interceptor;

import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import com.local.web.auth.AuthRequired;
import com.local.web.auth.UserAuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {
    private UserAuthUtil userAuthUtil;

    @Autowired
    public void setUserAuthUtil(UserAuthUtil userAuthUtil) {
        this.userAuthUtil = userAuthUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws InvalidTokenException, TokenExpiredException {
        AuthRequired authRequired;
        if(handler instanceof HandlerMethod controller && (authRequired = controller.getMethodAnnotation(AuthRequired.class)) != null){
            String token = request.getHeader("Authorization");
            Map<String, Object> tokenClaims = userAuthUtil.validateToken(token, authRequired);
            request.setAttribute("userId", ((Double) tokenClaims.get("userId")).intValue());
        }
        return true;
    }
}
