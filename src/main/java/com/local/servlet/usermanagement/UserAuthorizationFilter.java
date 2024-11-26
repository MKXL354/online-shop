package com.local.servlet.usermanagement;

import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import com.local.util.token.TokenManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class UserAuthorizationFilter implements Filter {
    protected TokenManager tokenManager;

    @Override
    public void init(FilterConfig filterConfig) {
        tokenManager = (TokenManager)filterConfig.getServletContext().getAttribute("tokenManager");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;
        String jws = httpServletRequest.getHeader("Authorization");
        Map<String, Object> claims = setClaims();
        try{
            Map<String, Object> tokenClaims = tokenManager.validateSignedToken(jws, claims);
            int userId = ((Double)tokenClaims.get("userId")).intValue();
            servletRequest.setAttribute("userId", userId);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch(InvalidTokenException | TokenExpiredException e){
            throw new ServletException(e);
        }
    }

    protected abstract HashMap<String, Object> setClaims();
}
