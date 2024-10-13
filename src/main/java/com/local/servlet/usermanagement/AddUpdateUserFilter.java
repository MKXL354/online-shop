package com.local.servlet.usermanagement;

import com.local.model.User;
import com.local.servlet.CommonWebComponentService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AddUpdateUserFilter implements Filter {
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        User user = commonWebComponentService.getObjectFromJsonRequest(servletRequest, User.class);
        String violationMessages = commonWebComponentService.validateObject(user);
        if(violationMessages.isEmpty()){
            servletRequest.setAttribute("user", user);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            commonWebComponentService.writeResponse(httpResponse, violationMessages);
        }
    }
}
