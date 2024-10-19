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
            Class<?> objectClass= getObjectClass(servletRequest);
            obj = commonWebComponentService.getObjectFromJsonRequest(servletRequest, objectClass);
        }
        catch(JsonFormatException e){
            throw new ServletException(e);
        }
        String violationMessages = objectValidator.validate(obj);
        if(violationMessages.isEmpty()){
            String objectName = getObjectName();
            servletRequest.setAttribute(objectName, obj);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            throw new ServletException(new InvalidRequestObjectException(violationMessages, null));
        }
    }

    protected abstract Class<?> getObjectClass(ServletRequest servletRequest);
    protected abstract String getObjectName();
}
