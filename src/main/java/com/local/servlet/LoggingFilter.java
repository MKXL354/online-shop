package com.local.servlet;

import com.local.util.logging.ActivityLog;
import com.local.util.logging.BaseLog;
import com.local.util.logging.BatchLogManager;
import com.local.util.logging.LogLevel;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoggingFilter implements Filter {
    private BatchLogManager batchLogManager;

    @Override
    public void init(FilterConfig filterConfig) {
        batchLogManager = (BatchLogManager)filterConfig.getServletContext().getAttribute("batchLogManager");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String clientIp = httpServletRequest.getRemoteAddr();
        String url = httpServletRequest.getRequestURL().toString();
        BaseLog log = new ActivityLog(clientIp, url);
        log.setRequestTime();

        try{
            filterChain.doFilter(servletRequest, servletResponse);
            if(httpServletResponse.getStatus() < 400){
                log.setLevel(LogLevel.OK);
            }
            else{
                log.setLevel(LogLevel.FAIL);
            }
        }
        catch(Exception e){
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().print("Internal Server Error");
            log.createExceptionLog(e);
        }
        finally{
            log.setResponseTime();
        }
        batchLogManager.addLog(log);
    }
}
