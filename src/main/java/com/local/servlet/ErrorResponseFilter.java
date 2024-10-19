package com.local.servlet;

import com.local.dto.ErrorResponse;
import com.local.util.PropertyManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseFilter implements Filter {
    private PropertyManager propertyManager;
    private CommonWebComponentService commonWebComponentService;

    @Override
    public void init(FilterConfig filterConfig) {
        propertyManager = (PropertyManager)filterConfig.getServletContext().getAttribute("errorResponsePropertyManager");
        commonWebComponentService = (CommonWebComponentService)filterConfig.getServletContext().getAttribute("commonWebComponentService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch(ServletException e){
            sendErrorResponse(servletResponse, e);
        }
    }

    private void sendErrorResponse(ServletResponse servletResponse, ServletException e) throws IOException, ServletException {
        Exception exception = (Exception)e.getRootCause();
        String errorResponseType = exception.getClass().getSimpleName();
        int statusCode;
        try{
            statusCode = Integer.parseInt(propertyManager.getProperty(errorResponseType));
        }
        catch(NumberFormatException nfe){
            throw new ServletException(nfe);
        }
        String message = exception.getMessage();
        ErrorResponse errorResponse = new ErrorResponse(statusCode, errorResponseType, message);
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        httpResponse.setStatus(statusCode);
        commonWebComponentService.writeResponse(httpResponse, errorResponse);
    }
}
