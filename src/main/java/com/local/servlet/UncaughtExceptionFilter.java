package com.local.servlet;

import com.local.util.ActivityLogger;
import com.local.util.BaseLogger;
import com.local.util.LogLevel;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

//TODO: rename or create separate filter for logging?
public class UncaughtExceptionFilter implements Filter {
    private String absoluteLogOutputDirectory;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String relativeLogOutputDirectory = filterConfig.getInitParameter("relativeLogOutputDirectory");
        this.absoluteLogOutputDirectory = filterConfig.getServletContext().getRealPath(relativeLogOutputDirectory);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException {
        LogLevel logLevel;
//        TODO: fill log message
        String message = null;
        String clientIp = servletRequest.getRemoteAddr();
        BaseLogger logger = new ActivityLogger(absoluteLogOutputDirectory, clientIp);
        try{
            filterChain.doFilter(servletRequest, servletResponse);
            logLevel = LogLevel.INFO;
        }
        catch(Exception e){
//            e.printStackTrace();
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            httpServletResponse.getWriter().print("Internal Server Error");
            logLevel = LogLevel.ERROR;
        }
        logger.setLevel(logLevel);
        logger.log();
    }
}
