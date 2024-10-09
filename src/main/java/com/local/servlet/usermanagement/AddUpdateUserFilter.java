package com.local.servlet.usermanagement;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.model.User;
import com.local.servlet.CommonWebComponentService;
import com.local.validator.ObjectValidator;
import com.local.validator.ValidatorException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;

public class AddUpdateUserFilter implements Filter {
    private CommonWebComponentService commonWebComponentService;
    private ObjectValidator validator;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
        validator = new ObjectValidator();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            User user = commonWebComponentService.getObjectFromJsonRequest(servletRequest, User.class);
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
                commonWebComponentService.writeResponse(httpResponse, result.toString());
            }
        }
        catch(ValidatorException e){
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
