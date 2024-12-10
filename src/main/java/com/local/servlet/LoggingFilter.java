package com.local.servlet;

import com.local.util.logging.LogLevel;
import com.local.util.logging.LogObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoggingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String clientIp = httpServletRequest.getRemoteAddr();
        String url = httpServletRequest.getRequestURL().toString();
        LogObject.Builder logBuilder = new LogObject.Builder().setClientIp(clientIp).setUrl(url).setStartTime(LocalDateTime.now());

        try{
            filterChain.doFilter(servletRequest, servletResponse);
            if(httpServletResponse.getStatus() < 400){
                logBuilder.setLevel(LogLevel.OK);
            }
            else{
                logBuilder.setLevel(LogLevel.FAIL);
            }
        }
        catch(Exception e){
            logBuilder.setLevel(LogLevel.ERROR);
            logBuilder.setThrowable(e);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().print("Internal Server Error");
        }
        finally{
            logBuilder.setCode(httpServletResponse.getStatus());
            logBuilder.setEndTime(LocalDateTime.now());
            logBuilder.build().submit();
        }
    }
}
