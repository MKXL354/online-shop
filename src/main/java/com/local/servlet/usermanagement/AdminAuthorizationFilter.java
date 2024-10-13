package com.local.servlet.usermanagement;

import com.local.model.UserType;
import com.local.servlet.CommonWebComponentService;
import com.local.util.token.InvalidTokenException;
import com.local.util.token.TokenExpiredException;
import jakarta.servlet.*;

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
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", UserType.ADMIN.toString());
        try{
            commonWebComponentService.validateToken(servletRequest, claims);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch(InvalidTokenException | TokenExpiredException e){
            throw new ServletException(e);
        }
    }
}
//TODO: maybe apply "template method" to remove duplication not composition
