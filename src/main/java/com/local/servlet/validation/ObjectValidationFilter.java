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
import java.util.concurrent.ConcurrentHashMap;

public class ObjectValidationFilter implements Filter {
    protected CommonWebComponentService commonWebComponentService;
    protected ObjectValidator objectValidator;
    private ConcurrentHashMap<String, ValidationDataHolder> validationData;

    @Override
    public void init(FilterConfig filterConfig) {
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
        objectValidator = (ObjectValidator)filterConfig.getServletContext().getAttribute("objectValidator");
        validationData = new ConcurrentHashMap<>();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object obj;
        String servletName = ((HttpServletRequest)servletRequest).getHttpServletMapping().getServletName();
        ValidationDataHolder holder = validationData.computeIfAbsent(servletName, (key) -> getAnnotationValues(servletName, servletRequest));
        try{
            Class<?> objectClass = holder.getObjectClass();
            obj = commonWebComponentService.getObjectFromJsonRequest(servletRequest, objectClass);
        }
        catch(JsonFormatException e){
            throw new ServletException(e);
        }
        String violationMessages = objectValidator.validate(obj);
        if(violationMessages.isEmpty()){
            String objectName = holder.getObjectName();
            servletRequest.setAttribute(objectName, obj);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        else{
            throw new ServletException(new InvalidRequestObjectException(violationMessages, null));
        }
    }

    private ValidationDataHolder getAnnotationValues(String servletName, ServletRequest servletRequest) {
        try {
            Class<?> servletClass = Class.forName(servletRequest.getServletContext().getServletRegistration(servletName).getClassName());
            Annotation annotation = servletClass.getDeclaredAnnotation(RequiresValidation.class);
            if(annotation == null){
                throw new ApplicationRuntimeException(servletName + " is not annotated for validation", null);
            }
            Class<?> objectClass = (Class<?>)RequiresValidation.class.getDeclaredMethod("objectClass").invoke(annotation);
            String objectName = (String)RequiresValidation.class.getDeclaredMethod("objectName").invoke(annotation);
            return new ValidationDataHolder(objectClass, objectName);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new ApplicationRuntimeException(e.getMessage(), e);
        }
    }
}
