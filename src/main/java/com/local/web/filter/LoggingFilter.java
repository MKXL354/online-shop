package com.local.web.filter;

import com.local.util.logging.LogLevel;
import com.local.util.logging.LogManager;
import com.local.util.logging.LogObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoggingFilter implements Filter {
    private LogManager logManager;

    @Override
    public void init(FilterConfig filterConfig) {
        ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
        logManager = context.getBean(LogManager.class);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String clientIp = httpServletRequest.getRemoteAddr();
        String url = httpServletRequest.getRequestURL().toString();
        LogObject.Builder logBuilder = new LogObject.Builder().setClientIp(clientIp).setUrl(url).setStartTime(LocalDateTime.now());

        filterChain.doFilter(servletRequest, servletResponse);

        int statusCode = httpServletResponse.getStatus();
        if(statusCode < 500){
            if(statusCode < 400){
                logBuilder.setLevel(LogLevel.OK);
            }
            else{
                logBuilder.setLevel(LogLevel.FAIL);
            }
            logManager.submit(logBuilder.setCode(statusCode).setEndTime(LocalDateTime.now()).build());
        }
    }
}
