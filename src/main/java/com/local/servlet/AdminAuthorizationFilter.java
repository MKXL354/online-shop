package com.local.servlet;

import com.local.model.UserType;
import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminAuthorizationFilter implements Filter {
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", UserType.ADMIN.toString());
        try{
            commonWebComponentService.validateToken(servletRequest, claims);
            filterChain.doFilter(servletRequest, servletResponse);
        }
//        TODO: maybe catch expired for refreshing the token
        catch(InvalidTokenException | TokenExpiredException e){
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpServletResponse.getWriter().print(e.getMessage());
        }
    }
}
