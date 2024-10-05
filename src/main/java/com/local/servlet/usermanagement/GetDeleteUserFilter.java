package com.local.servlet.usermanagement;

import com.local.servlet.CommonServletServices;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class GetDeleteUserFilter implements Filter {
    private CommonServletServices commonServletServices;

    @Override
    public void init(FilterConfig filterConfig) {
        commonServletServices = (CommonServletServices)filterConfig.getServletContext().getAttribute("commonServletServices");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try{
            int id = Integer.parseInt(servletRequest.getParameter("id"));
            servletRequest.setAttribute("id", id);
            filterChain.doFilter(servletRequest, servletResponse);
        }
        catch(NumberFormatException e){
            HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            commonServletServices.writeResponse(httpResponse, "bad id format");
        }
    }
}
//TODO: currently only used for delete, which might also be changed later
