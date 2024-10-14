package com.local.servlet;

import com.local.util.objectvalidator.ObjectValidator;
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
        Object obj;
        try{
            obj = getObjectToValidate(servletRequest);
        }
        catch(JsonFormatException e){
            throw new ServletException(e);
        }
        String violationMessages = objectValidator.validate(obj);
        if(violationMessages.isEmpty()){
            setObjectAsAttribute(servletRequest);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            System.out.println(violationMessages);
            throw new ServletException(new InvalidRequestObjectException(violationMessages, null));
        }
    }

    protected abstract Object getObjectToValidate(ServletRequest servletRequest) throws IOException, JsonFormatException;
    protected abstract void setObjectAsAttribute(ServletRequest servletRequest);
}
//TODO: redesign 400 bad request
