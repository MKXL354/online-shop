package com.local.servlet;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.servlet.usermanagement.InvalidUserRequestObjectException;
import com.local.util.objectvalidator.ObjectValidator;
import com.local.util.objectvalidator.ValidatorException;
import jakarta.servlet.*;

import java.io.IOException;

public abstract class ObjectValidationFilter implements Filter {
    protected CommonWebComponentService commonWebComponentService;
    protected ObjectValidator objectValidator;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
        objectValidator = (ObjectValidator)filterConfig.getServletContext().getAttribute("objectValidator");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object obj = getObjectToValidate(servletRequest);
        try {
            String violationMessages = objectValidator.validate(obj);
            if(violationMessages.isEmpty()){
                setObjectAsAttribute(servletRequest);
                filterChain.doFilter(servletRequest, servletResponse);
            }
            else{
                System.out.println(violationMessages);
                throw new ServletException(new InvalidUserRequestObjectException(violationMessages, null));
            }
        } catch (ValidatorException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }

    protected abstract Object getObjectToValidate(ServletRequest servletRequest) throws IOException;
    protected abstract void setObjectAsAttribute(ServletRequest servletRequest);
}
