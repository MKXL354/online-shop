package com.local.servlet.usermanagement;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.model.User;
import com.local.servlet.CommonServletServices;
import com.local.validator.ObjectValidator;
import com.local.validator.ValidatorException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;

public class UserManagementFilter implements Filter {
    private CommonServletServices commonServletServices;
    private ObjectValidator validator;

    @Override
    public void init(FilterConfig filterConfig) {
        commonServletServices = (CommonServletServices)filterConfig.getServletContext().getAttribute("commonServletServices");
        validator = new ObjectValidator();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            User user = commonServletServices.getObjectFromJsonRequest(servletRequest, User.class);
            HashSet<String> violationMessages = validator.validate(user);
            if(violationMessages.isEmpty()){
                servletRequest.setAttribute("user", user);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else{
                StringBuilder result = new StringBuilder();
                violationMessages.forEach(result::append);
                HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                commonServletServices.writeResponse(httpResponse, result.toString());
            }
        }
        catch(ValidatorException e){
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
