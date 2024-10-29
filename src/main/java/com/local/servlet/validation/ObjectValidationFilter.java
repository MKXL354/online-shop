package com.local.servlet.validation;

import com.local.commonexceptions.ApplicationRuntimeException;
import com.local.servlet.CommonWebComponentService;
import com.local.servlet.InvalidRequestObjectException;
import com.local.servlet.JsonFormatException;
import com.local.util.objectvalidator.ObjectValidator;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectValidationFilter implements Filter {
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
            Class<?> objectClass = getObjectClass(servletRequest);
            obj = commonWebComponentService.getObjectFromJsonRequest(servletRequest, objectClass);
        }
        catch(JsonFormatException e){
            throw new ServletException(e);
        }
        String violationMessages = objectValidator.validate(obj);
        if(violationMessages.isEmpty()){
            String objectName = getObjectName(servletRequest);
            servletRequest.setAttribute(objectName, obj);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            throw new ServletException(new InvalidRequestObjectException(violationMessages, null));
        }
    }

    private Class<?> getObjectClass(ServletRequest servletRequest) {
        return (Class<?>)getAnnotationValues(servletRequest, "objectClass");
    }

    private String getObjectName(ServletRequest servletRequest) {
        return (String)getAnnotationValues(servletRequest, "objectName");
    }

    private Object getAnnotationValues(ServletRequest servletRequest, String valueName) {
        String servletName = ((HttpServletRequest)servletRequest).getHttpServletMapping().getServletName();
        try {
            Class<?> servletClass = Class.forName(servletRequest.getServletContext().getServletRegistration(servletName).getClassName());
            Annotation annotation = servletClass.getDeclaredAnnotation(RequiresValidation.class);
            if(annotation == null){
                throw new ApplicationRuntimeException(servletName + " is not annotated for validation", null);
            }
            Method method = RequiresValidation.class.getDeclaredMethod(valueName);
            return method.invoke(annotation);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
//TODO: maybe cache the results and configure these at startup?
